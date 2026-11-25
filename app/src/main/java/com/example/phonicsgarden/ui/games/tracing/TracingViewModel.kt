package com.example.phonicsgarden.ui.games.tracing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonicsgarden.data.local.entity.Grapheme
import com.example.phonicsgarden.data.repository.ProgressRepository
import com.example.phonicsgarden.data.repository.SettingsRepository
import com.example.phonicsgarden.data.repository.WordRepository
import com.example.phonicsgarden.engine.LetterShapes
import com.example.phonicsgarden.engine.TracePoint
import com.example.phonicsgarden.engine.TraceValidator
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

data class TracingState(
    val letter: String = "",
    val spokenSound: String = "",
    /** The guide strokes to trace, in writing order. */
    val guides: List<List<TracePoint>> = emptyList(),
    /** Strokes the child has finished so far for this letter. */
    val completedStrokes: List<List<TracePoint>> = emptyList(),
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
class TracingViewModel @Inject constructor(
    private val wordRepository: WordRepository,
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val speaker: TtsSpeaker
) : ViewModel() {

    private val _state = MutableStateFlow(TracingState())
    val state: StateFlow<TracingState> = _state.asStateFlow()

    private var targets: List<Grapheme> = emptyList()
    private var index = 0
    private var soundEnabled = true
    private var firstTry = true

    init {
        viewModelScope.launch { startRound() }
    }

    private suspend fun startRound() {
        wordRepository.seedIfEmpty()
        val settings = settingsRepository.settings.first()
        soundEnabled = settings.soundEnabled
        _state.update { it.copy(reducedMotion = settings.reducedMotion) }

        val boxes = progressRepository.masteryBoxes()
        targets = wordRepository.graphemesUpToPhase(settings.phase)
            .filter { LetterShapes.hasShape(it.letter) }
            .shuffled()
            .sortedBy { boxes[it.id] ?: 0 }
            .take(ROUND_LENGTH)

        if (targets.isEmpty()) {
            _state.update { it.copy(noContent = true) }
        } else {
            showLetter()
        }
    }

    private fun showLetter() {
        val target = targets[index]
        firstTry = true
        _state.update {
            it.copy(
                letter = target.letter,
                spokenSound = target.spokenSound,
                guides = LetterShapes.strokes(target.letter).orEmpty(),
                completedStrokes = emptyList(),
                questionNumber = index + 1,
                total = targets.size,
                feedback = null,
                locked = false
            )
        }
        speakPrompt()
    }

    fun replayPrompt() = speakPrompt()

    /** Called by the screen each time the child lifts their finger. Points are normalised 0..1. */
    fun onStrokeFinished(stroke: List<TracePoint>) {
        val current = _state.value
        if (current.locked || stroke.isEmpty() || current.guides.isEmpty()) return

        val strokes = current.completedStrokes + listOf(stroke)
        val valid = TraceValidator.isValid(
            path = strokes.flatten(),
            waypoints = current.guides.flatten(),
            tolerance = TOLERANCE
        )

        when {
            valid -> viewModelScope.launch { onSuccess(strokes) }
            strokes.size >= current.guides.size -> viewModelScope.launch { onMiss() }
            else -> _state.update { it.copy(completedStrokes = strokes) } // wait for more strokes
        }
    }

    private suspend fun onSuccess(strokes: List<List<TracePoint>>) {
        val grapheme = targets[index]
        progressRepository.recordAttempt(grapheme.id, GAME_NAME, correct = true)
        _state.update {
            it.copy(
                completedStrokes = strokes,
                feedback = Feedback.CORRECT,
                locked = true,
                correctCount = it.correctCount + if (firstTry) 1 else 0
            )
        }
        speak("Well done")
        delay(FEEDBACK_DELAY_MS)
        advance()
    }

    private suspend fun onMiss() {
        firstTry = false
        progressRepository.recordAttempt(targets[index].id, GAME_NAME, correct = false)
        // Gently clear the page so the child can try again.
        _state.update { it.copy(feedback = Feedback.TRY_AGAIN, completedStrokes = emptyList()) }
    }

    fun startAgain() {
        _state.update { it.copy(completedStrokes = emptyList(), feedback = null) }
    }

    /** Skipping is always allowed and is not recorded as a mistake. */
    fun skip() {
        if (_state.value.locked) return
        advance()
    }

    private fun advance() {
        index++
        if (index >= targets.size) {
            _state.update { it.copy(finished = true) }
        } else {
            showLetter()
        }
    }

    private fun speakPrompt() = speak("Let's write the sound ${_state.value.spokenSound}")

    private fun speak(text: String) {
        if (soundEnabled) speaker.speak(text)
    }

    companion object {
        const val GAME_NAME = "tracing"
        const val ROUND_LENGTH = 4 // tracing takes longer, so shorter rounds
        private const val TOLERANCE = 0.15f // forgiving for small hands
        private const val FEEDBACK_DELAY_MS = 1500L
    }
}
