package com.example.phonicsgarden.ui.games.monster

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonicsgarden.data.repository.ProgressRepository
import com.example.phonicsgarden.data.repository.SettingsRepository
import com.example.phonicsgarden.data.repository.WordRepository
import com.example.phonicsgarden.engine.FeedChallenge
import com.example.phonicsgarden.engine.WordChallengeGenerator
import com.example.phonicsgarden.ui.games.Feedback
import com.example.phonicsgarden.util.TtsSpeaker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FeedMonsterState(
    val challenge: FeedChallenge? = null,
    val questionNumber: Int = 0,
    val total: Int = 0,
    val correctCount: Int = 0,
    val feedback: Feedback? = null,
    val locked: Boolean = false,
    val finished: Boolean = false,
    val noContent: Boolean = false,
    val reducedMotion: Boolean = false
)

@HiltViewModel
class FeedMonsterViewModel @Inject constructor(
    private val wordRepository: WordRepository,
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val generator: WordChallengeGenerator,
    private val speaker: TtsSpeaker
) : ViewModel() {

    private val _state = MutableStateFlow(FeedMonsterState())
    val state: StateFlow<FeedMonsterState> = _state.asStateFlow()

    private var challenges: List<FeedChallenge> = emptyList()
    private var index = 0
    private var soundEnabled = true
    private var answeredFirstTry = true

    init {
        viewModelScope.launch { startRound() }
    }

    private suspend fun startRound() {
        wordRepository.seedIfEmpty()
        val settings = settingsRepository.settings.first()
        soundEnabled = settings.soundEnabled
        _state.update { it.copy(reducedMotion = settings.reducedMotion) }

        val graphemes = wordRepository.graphemesUpToPhase(settings.phase)
        val words = wordRepository.pictureWordsUpToPhase(settings.phase)
        val boxes = progressRepository.masteryBoxes()

        // Least-known sounds first; one challenge per distinct sound (c and k share "kuh").
        challenges = graphemes.shuffled()
            .sortedBy { boxes[it.id] ?: 0 }
            .mapNotNull { generator.generate(it, graphemes, words) }
            .distinctBy { it.prompt }
            .take(ROUND_LENGTH)

        if (challenges.isEmpty()) {
            _state.update { it.copy(noContent = true) }
        } else {
            showChallenge()
        }
    }

    private fun showChallenge() {
        val challenge = challenges[index]
        answeredFirstTry = true
        _state.update {
            it.copy(
                challenge = challenge,
                questionNumber = index + 1,
                total = challenges.size,
                feedback = null,
                locked = false
            )
        }
        speak(challenge.prompt)
    }

    fun replayPrompt() {
        _state.value.challenge?.let { speak(it.prompt) }
    }

    fun onFed(word: String) {
        val current = _state.value
        val challenge = current.challenge ?: return
        if (current.locked) return

        val correct = challenge.isCorrect(word)
        viewModelScope.launch {
            progressRepository.recordAttempt(challenge.graphemeId, GAME_NAME, correct)

            if (correct) {
                _state.update {
                    it.copy(
                        feedback = Feedback.CORRECT,
                        locked = true,
                        correctCount = it.correctCount + if (answeredFirstTry) 1 else 0
                    )
                }
                speak(word) // the monster says the word it just ate
                delay(FEEDBACK_DELAY_MS)
                index++
                if (index >= challenges.size) {
                    _state.update { it.copy(finished = true) }
                } else {
                    showChallenge()
                }
            } else {
                answeredFirstTry = false
                _state.update { it.copy(feedback = Feedback.TRY_AGAIN) }
                speak(challenge.prompt)
            }
        }
    }

    private fun speak(text: String) {
        if (soundEnabled) speaker.speak(text)
    }

    companion object {
        const val GAME_NAME = "feed_monster"
        const val ROUND_LENGTH = 6
        private const val FEEDBACK_DELAY_MS = 1200L
    }
}
