package com.example.phonicsgarden.data.repository

import com.example.phonicsgarden.data.local.SeedData
import com.example.phonicsgarden.data.local.WordPictures
import com.example.phonicsgarden.data.local.dao.GraphemeDao
import com.example.phonicsgarden.data.local.dao.WordDao
import com.example.phonicsgarden.data.local.entity.Grapheme
import com.example.phonicsgarden.data.local.entity.Word
import com.example.phonicsgarden.data.remote.DatamuseApi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface WordRepository {
    fun observeGraphemes(): Flow<List<Grapheme>>
    suspend fun graphemesUpToPhase(phase: Int): List<Grapheme>
    suspend fun wordsStartingWith(letter: String): List<Word>

    /** Only words that have a picture (child-safe allowlist), for the given phase and below. */
    suspend fun pictureWordsUpToPhase(phase: Int): List<Word>
    suspend fun seedIfEmpty()

    /** Returns true if the network refresh succeeded. Never throws for network problems. */
    suspend fun refreshWordsFromNetwork(): Boolean
}

@Singleton
class WordRepositoryImpl @Inject constructor(
    private val graphemeDao: GraphemeDao,
    private val wordDao: WordDao,
    private val api: DatamuseApi
) : WordRepository {

    override fun observeGraphemes(): Flow<List<Grapheme>> = graphemeDao.observeAll()

    override suspend fun graphemesUpToPhase(phase: Int): List<Grapheme> =
        graphemeDao.getUpToPhase(phase)

    override suspend fun wordsStartingWith(letter: String): List<Word> =
        wordDao.byFirstLetter(letter)

    override suspend fun pictureWordsUpToPhase(phase: Int): List<Word> =
        wordDao.upToPhase(phase).filter { WordPictures.emojiFor(it.text) != null }

    override suspend fun seedIfEmpty() {
        if (graphemeDao.count() == 0) graphemeDao.insertAll(SeedData.graphemes)
        // Words use IGNORE on conflict, so this is cheap and also adds newly bundled words.
        wordDao.insertAll(SeedData.words)
    }

    /**
     * Fetches extra three-letter words for phase 1 sounds.
     * ETHICS: Datamuse results are NOT child-safe by default. They are stored, but only words
     * that also appear in [WordPictures] are ever shown to children (see pictureWordsUpToPhase).
     */
    override suspend fun refreshWordsFromNetwork(): Boolean = try {
        SeedData.graphemes.filter { it.phase == 1 }.forEach { grapheme ->
            val results = api.words(pattern = "${grapheme.letter}??", max = 20)
            val words = results
                .map { it.word.lowercase() }
                .filter { it.length == 3 && it.all(Char::isLetter) }
                .map { Word(text = it, firstLetter = grapheme.letter, phase = grapheme.phase) }
            wordDao.insertAll(words)
        }
        true
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        false // offline or API down: keep using bundled data
    }
}
