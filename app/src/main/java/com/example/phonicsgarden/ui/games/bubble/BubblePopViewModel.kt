package com.example.phonicsgarden.ui.games.bubble

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonicsgarden.data.local.entity.Grapheme
import com.example.phonicsgarden.data.repository.ProgressRepository
import com.example.phonicsgarden.data.repository.SettingsRepository
import com.example.phonicsgarden.data.repository.WordRepository
import com.example.phonicsgarden.engine.Challenge
import com.example.phonicsgarden.engine.ChallengeGenerator
import com.example.phonicsgarden.ui.games.Feedback
import com.example.phonicsgarden.util.SoundEffect
import com.example.phonicsgarden.util.SoundEffects
import com.example.phonicsgarden.util.Speaker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BubblePopState(
    val challenge: Challenge? = null,
    val questionNumber: Int = 0,
    val total: Int = 0,
    val correctCount: Int = 0,
    val feedback: Feedback? = null,
    val locked: Boolean = false,
    val finished: Boolean = false
)

@HiltViewModel
class BubblePopViewModel @Inject constructor(
    private val wordRepository: WordRepository,
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val generator: ChallengeGenerator,
    private val speaker: Speaker,
    private val effects: SoundEffects
) : ViewModel() {

    private val _state = MutableStateFlow(BubblePopState())
    val state: StateFlow<BubblePopState> = _state.asStateFlow()

    private var pool: List<Grapheme> = emptyList()
    private var targets: List<Grapheme> = emptyList()
    private var index = 0
    private var soundEnabled = true
    private var answeredFirstTry = true

    init {
        viewModelScope.launch { startRound() }
    }

    private suspend fun startRound() {
        wordRepository.seedIfEmpty() // safe if already seeded
        val settings = settingsRepository.settings.first()
        soundEnabled = settings.soundEnabled
        pool = wordRepository.graphemesUpToPhase(settings.phase)

        // Sounds the child knows least come first (stable sort keeps the shuffle within a box).
        val boxes = progressRepository.masteryBoxes()
        targets = pool.shuffled().sortedBy { boxes[it.id] ?: 0 }.take(ROUND_LENGTH)
        if (targets.isEmpty()) return
        showChallenge()
    }

    private fun showChallenge() {
        val challenge = generator.generate(targets[index], pool)
        answeredFirstTry = true
        _state.update {
            it.copy(
                challenge = challenge,
                questionNumber = index + 1,
                total = targets.size,
                feedback = null,
                locked = false
            )
        }
        speak(challenge.prompt)
    }

    fun replaySound() {
        _state.value.challenge?.let { speak(it.prompt) }
    }

    fun onAnswer(letter: String) {
        val current = _state.value
        val challenge = current.challenge ?: return
        if (current.locked) return

        val correct = challenge.isCorrect(letter)
        viewModelScope.launch {
            progressRepository.recordAttempt(challenge.graphemeId, GAME_NAME, correct)

            if (correct) {
                playEffect(SoundEffect.POP)
                _state.update {
                    it.copy(
                        feedback = Feedback.CORRECT,
                        locked = true,
                        correctCount = it.correctCount + if (answeredFirstTry) 1 else 0
                    )
                }
                delay(FEEDBACK_DELAY_MS)
                index++
                if (index >= targets.size) {
                    playEffect(SoundEffect.FANFARE)
                    _state.update { it.copy(finished = true) }
                } else {
                    showChallenge()
                }
            } else {
                answeredFirstTry = false
                playEffect(SoundEffect.SOFT)
                _state.update { it.copy(feedback = Feedback.TRY_AGAIN) }
                speak(challenge.prompt) // gentle repeat, no harsh sounds
            }
        }
    }

    private fun speak(text: String) {
        if (soundEnabled) speaker.speak(text)
    }

    private fun playEffect(effect: SoundEffect) {
        if (soundEnabled) effects.play(effect)
    }

    companion object {
        const val GAME_NAME = "bubble_pop"
        const val ROUND_LENGTH = 6
        private const val FEEDBACK_DELAY_MS = 900L
    }
}
