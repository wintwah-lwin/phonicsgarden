package com.example.phonicsgarden.data.repository

import com.example.phonicsgarden.data.local.SeedData
import com.example.phonicsgarden.data.local.dao.GraphemeDao
import com.example.phonicsgarden.data.local.dao.WordDao
import com.example.phonicsgarden.data.local.entity.Grapheme
import com.example.phonicsgarden.data.local.entity.Word
import com.example.phonicsgarden.data.remote.WordPackApi
import com.example.phonicsgarden.engine.PackEntry
import com.example.phonicsgarden.engine.WordPackValidator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/** Outcome of checking the online word pack. */
sealed interface WordPackSyncResult {
    /** [added] words were new; [rejected] entries failed validation and were ignored. */
    data class Success(val packVersion: Int, val added: Int, val rejected: Int) : WordPackSyncResult
    data object Failed : WordPackSyncResult
}

interface WordRepository {
    fun observeGraphemes(): Flow<List<Grapheme>>
    suspend fun graphemesUpToPhase(phase: Int): List<Grapheme>
    suspend fun wordsStartingWith(letter: String): List<Word>

    /** Words (all of which have pictures) for the given phase and below. */
    suspend fun wordsUpToPhase(phase: Int): List<Word>

    /** How many extra words have been downloaded from the online word pack. */
    fun observeDownloadedWordCount(): Flow<Int>

    suspend fun seedIfEmpty()

    /** Downloads, validates and stores the online word pack. Never throws for network problems. */
    suspend fun syncWordPack(): WordPackSyncResult
}

@Singleton
class WordRepositoryImpl @Inject constructor(
    private val graphemeDao: GraphemeDao,
    private val wordDao: WordDao,
    private val api: WordPackApi
) : WordRepository {

    override fun observeGraphemes(): Flow<List<Grapheme>> = graphemeDao.observeAll()

    override suspend fun graphemesUpToPhase(phase: Int): List<Grapheme> =
        graphemeDao.getUpToPhase(phase)

    override suspend fun wordsStartingWith(letter: String): List<Word> =
        wordDao.byFirstLetter(letter)

    override suspend fun wordsUpToPhase(phase: Int): List<Word> = wordDao.upToPhase(phase)

    override fun observeDownloadedWordCount(): Flow<Int> =
        wordDao.observeCountBySource(Word.SOURCE_PACK)

    override suspend fun seedIfEmpty() {
        if (graphemeDao.count() == 0) graphemeDao.insertAll(SeedData.graphemes)
        // Words are inserted with IGNORE on conflict, so this is cheap and adds new bundled words.
        wordDao.insertAll(SeedData.words)
    }

    override suspend fun syncWordPack(): WordPackSyncResult = try {
        val pack = api.wordPack()
        val taughtLetters = graphemeDao.getAll().associate { it.letter to it.phase }

        // Never trust downloaded content: validate every entry before it is stored.
        val validation = WordPackValidator.validate(
            entries = pack.words.map { PackEntry(it.text, it.emoji) },
            taughtLetters = taughtLetters
        )
        val rowIds = wordDao.insertAll(
            validation.accepted.map {
                Word(
                    text = it.text,
                    firstLetter = it.firstLetter,
                    phase = it.phase,
                    emoji = it.emoji,
                    source = Word.SOURCE_PACK
                )
            }
        )
        WordPackSyncResult.Success(
            packVersion = pack.version,
            added = rowIds.count { it != -1L },
            rejected = validation.rejected
        )
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        WordPackSyncResult.Failed // offline or the file is unavailable: bundled words still work
    }
}
