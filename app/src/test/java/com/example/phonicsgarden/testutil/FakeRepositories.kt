package com.example.phonicsgarden.testutil

import com.example.phonicsgarden.data.local.SeedData
import com.example.phonicsgarden.data.local.entity.Grapheme
import com.example.phonicsgarden.data.local.entity.Word
import com.example.phonicsgarden.data.repository.AppSettings
import com.example.phonicsgarden.data.repository.AttemptSummary
import com.example.phonicsgarden.data.repository.ProgressRepository
import com.example.phonicsgarden.data.repository.SettingsRepository
import com.example.phonicsgarden.data.repository.WordPackSyncResult
import com.example.phonicsgarden.data.repository.WordRepository
import com.example.phonicsgarden.engine.DayActivity
import com.example.phonicsgarden.util.SoundEffect
import com.example.phonicsgarden.util.SoundEffects
import com.example.phonicsgarden.util.Speaker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

class FakeWordRepository(
    val graphemesList: List<Grapheme> = SeedData.graphemes,
    val wordsList: List<Word> = SeedData.words
) : WordRepository {

    private val graphemesFlow = MutableStateFlow(graphemesList)

    /** What the next [syncWordPack] call returns. */
    var syncResult: WordPackSyncResult = WordPackSyncResult.Success(packVersion = 1, added = 3, rejected = 0)
    val downloadedWordCount = MutableStateFlow(0)

    override fun observeGraphemes(): Flow<List<Grapheme>> = graphemesFlow.asStateFlow()

    override suspend fun graphemesUpToPhase(phase: Int): List<Grapheme> =
        graphemesList.filter { it.phase <= phase }

    override suspend fun wordsStartingWith(letter: String): List<Word> =
        wordsList.filter { it.firstLetter == letter }

    override suspend fun wordsUpToPhase(phase: Int): List<Word> =
        wordsList.filter { it.phase <= phase }

    override fun observeDownloadedWordCount(): Flow<Int> = downloadedWordCount.asStateFlow()

    override suspend fun seedIfEmpty() {}

    override suspend fun syncWordPack(): WordPackSyncResult = syncResult
}

class FakeProgressRepository : ProgressRepository {
    private val masteryMap = MutableStateFlow<Map<Long, Int>>(emptyMap())
    private val totalAttempts = MutableStateFlow(0)
    private val correctAttempts = MutableStateFlow(0)
    val recordedAttempts = mutableListOf<Triple<Long, String, Boolean>>()

    override fun observeMasteryBoxes(): Flow<Map<Long, Int>> = masteryMap.asStateFlow()

    override suspend fun masteryBoxes(): Map<Long, Int> = masteryMap.value

    override suspend fun recordAttempt(graphemeId: Long, game: String, correct: Boolean) {
        recordedAttempts.add(Triple(graphemeId, game, correct))
        totalAttempts.update { it + 1 }
        if (correct) {
            correctAttempts.update { it + 1 }
            val currentBox = masteryMap.value[graphemeId] ?: 0
            masteryMap.update { it + (graphemeId to (currentBox + 1).coerceAtMost(4)) }
        }
    }

    override fun observeAttemptSummary(): Flow<AttemptSummary> =
        combine(totalAttempts, correctAttempts) { total, correct ->
            AttemptSummary(total, correct)
        }

    private val lastSevenDays = MutableStateFlow<List<DayActivity>>(emptyList())

    override fun observeLastSevenDays(): Flow<List<DayActivity>> = lastSevenDays.asStateFlow()

    fun setLastSevenDays(days: List<DayActivity>) {
        lastSevenDays.value = days
    }

    fun setMasteryBox(graphemeId: Long, box: Int) {
        masteryMap.update { it + (graphemeId to box) }
    }
}

class FakeSettingsRepository(
    initialSettings: AppSettings = AppSettings()
) : SettingsRepository {
    private val state = MutableStateFlow(initialSettings)

    override val settings: Flow<AppSettings> = state.asStateFlow()

    override suspend fun setPhase(phase: Int) {
        state.update { it.copy(phase = phase) }
    }

    override suspend fun setSoundEnabled(enabled: Boolean) {
        state.update { it.copy(soundEnabled = enabled) }
    }

    override suspend fun setReducedMotion(enabled: Boolean) {
        state.update { it.copy(reducedMotion = enabled) }
    }
}

class FakeSoundEffects : SoundEffects {
    val played = mutableListOf<SoundEffect>()

    override fun play(effect: SoundEffect) {
        played.add(effect)
    }
}

class FakeTtsSpeaker : Speaker {
    val spokenTexts = mutableListOf<String>()

    override fun speak(text: String) {
        spokenTexts.add(text)
    }
}
