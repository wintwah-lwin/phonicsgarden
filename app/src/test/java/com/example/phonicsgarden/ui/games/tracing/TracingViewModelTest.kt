package com.example.phonicsgarden.ui.games.tracing

import com.example.phonicsgarden.data.local.entity.Grapheme
import com.example.phonicsgarden.data.repository.AppSettings
import com.example.phonicsgarden.engine.LetterShapes
import com.example.phonicsgarden.testutil.FakeProgressRepository
import com.example.phonicsgarden.testutil.FakeSettingsRepository
import com.example.phonicsgarden.testutil.FakeTtsSpeaker
import com.example.phonicsgarden.testutil.FakeWordRepository
import com.example.phonicsgarden.testutil.MainDispatcherRule
import com.example.phonicsgarden.testutil.densify
import com.example.phonicsgarden.testutil.scribble
import com.example.phonicsgarden.ui.games.Feedback
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TracingViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val progressRepo = FakeProgressRepository()
    private val speaker = FakeTtsSpeaker()

    /** Only the letter "t" (two strokes), so multi-stroke behaviour is deterministic. */
    private val onlyT = FakeWordRepository(graphemesList = listOf(Grapheme(3, "t", "tuh", 1)))

    private fun createViewModel(
        wordRepo: FakeWordRepository = FakeWordRepository(),
        settings: AppSettings = AppSettings()
    ) = TracingViewModel(wordRepo, progressRepo, FakeSettingsRepository(settings), speaker)

    private fun TracingViewModel.traceCurrentLetterCorrectly() {
        val letter = this.state.value.letter
        LetterShapes.strokes(letter)!!.forEach { onStrokeFinished(densify(it)) }
    }

    @Test
    fun startRound_showsFirstLetterWithGuides() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.letter.isNotEmpty())
        assertTrue(state.guides.isNotEmpty())
        assertEquals(TracingViewModel.ROUND_LENGTH, state.total)
        assertEquals(1, state.questionNumber)
        assertTrue(speaker.spokenTexts.last().startsWith("Let's write the sound"))
    }

    @Test
    fun tracingCorrectly_recordsSuccessThenMovesOn() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.traceCurrentLetterCorrectly()

        assertEquals(Feedback.CORRECT, viewModel.state.value.feedback)
        assertTrue(viewModel.state.value.locked)
        assertEquals(1, progressRepo.recordedAttempts.size)
        assertEquals(TracingViewModel.GAME_NAME, progressRepo.recordedAttempts.first().second)
        assertTrue(progressRepo.recordedAttempts.first().third)
        assertTrue(speaker.spokenTexts.contains("Well done"))

        advanceUntilIdle()
        assertEquals(2, viewModel.state.value.questionNumber)
    }

    @Test
    fun scribbling_afterAllStrokes_showsTryAgainAndClearsThePage() = runTest {
        val viewModel = createViewModel(onlyT)
        advanceUntilIdle()

        repeat(viewModel.state.value.guides.size) { viewModel.onStrokeFinished(scribble) }

        assertEquals(Feedback.TRY_AGAIN, viewModel.state.value.feedback)
        assertTrue(viewModel.state.value.completedStrokes.isEmpty())
        assertFalse(viewModel.state.value.locked)
        assertEquals(1, progressRepo.recordedAttempts.size)
        assertFalse(progressRepo.recordedAttempts.first().third)
    }

    @Test
    fun multiStrokeLetter_waitsForRemainingStrokes() = runTest {
        val viewModel = createViewModel(onlyT)
        advanceUntilIdle()
        val strokes = LetterShapes.strokes("t")!!

        viewModel.onStrokeFinished(densify(strokes[0]))
        assertNull(viewModel.state.value.feedback)
        assertEquals(1, viewModel.state.value.completedStrokes.size)
        assertTrue(progressRepo.recordedAttempts.isEmpty())

        viewModel.onStrokeFinished(densify(strokes[1]))
        assertEquals(Feedback.CORRECT, viewModel.state.value.feedback)
    }

    @Test
    fun startAgain_clearsStrokesDrawnSoFar() = runTest {
        val viewModel = createViewModel(onlyT)
        advanceUntilIdle()

        viewModel.onStrokeFinished(densify(LetterShapes.strokes("t")!![0]))
        viewModel.startAgain()

        assertTrue(viewModel.state.value.completedStrokes.isEmpty())
    }

    @Test
    fun skip_movesOnWithoutRecordingAnAttempt() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.skip()

        assertEquals(2, viewModel.state.value.questionNumber)
        assertTrue(progressRepo.recordedAttempts.isEmpty())
    }

    @Test
    fun completingEveryLetter_finishesTheRoundWithFirstTryScore() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        repeat(TracingViewModel.ROUND_LENGTH) {
            viewModel.traceCurrentLetterCorrectly()
            advanceUntilIdle()
        }

        assertTrue(viewModel.state.value.finished)
        assertEquals(TracingViewModel.ROUND_LENGTH, viewModel.state.value.correctCount)
    }

    @Test
    fun soundOff_neverSpeaks() = runTest {
        createViewModel(settings = AppSettings(soundEnabled = false))
        advanceUntilIdle()

        assertTrue(speaker.spokenTexts.isEmpty())
    }

    @Test
    fun noLettersWithGuides_showsNoContent() = runTest {
        val noGuides = FakeWordRepository(graphemesList = listOf(Grapheme(99, "z", "zzz", 1)))
        val viewModel = createViewModel(noGuides)
        advanceUntilIdle()

        assertTrue(viewModel.state.value.noContent)
    }
}
