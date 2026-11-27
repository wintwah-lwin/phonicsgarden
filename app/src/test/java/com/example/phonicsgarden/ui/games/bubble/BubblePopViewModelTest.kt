package com.example.phonicsgarden.ui.games.bubble

import com.example.phonicsgarden.engine.ChallengeGenerator
import com.example.phonicsgarden.testutil.FakeProgressRepository
import com.example.phonicsgarden.testutil.FakeSettingsRepository
import com.example.phonicsgarden.testutil.FakeTtsSpeaker
import com.example.phonicsgarden.testutil.FakeWordRepository
import com.example.phonicsgarden.testutil.MainDispatcherRule
import com.example.phonicsgarden.ui.games.Feedback
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BubblePopViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun startRound_initializesChallengeAndTotal() = runTest {
        val wordRepo = FakeWordRepository()
        val progressRepo = FakeProgressRepository()
        val settingsRepo = FakeSettingsRepository()
        val generator = ChallengeGenerator()
        val speaker = FakeTtsSpeaker()

        val viewModel = BubblePopViewModel(wordRepo, progressRepo, settingsRepo, generator, speaker)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertNotNull(state.challenge)
        assertEquals(BubblePopViewModel.ROUND_LENGTH, state.total)
        assertEquals(1, state.questionNumber)
        assertTrue(speaker.spokenTexts.isNotEmpty())
    }

    @Test
    fun onAnswer_correct_recordsAttemptAndSetsCorrectFeedback() = runTest {
        val wordRepo = FakeWordRepository()
        val progressRepo = FakeProgressRepository()
        val settingsRepo = FakeSettingsRepository()
        val generator = ChallengeGenerator()
        val speaker = FakeTtsSpeaker()

        val viewModel = BubblePopViewModel(wordRepo, progressRepo, settingsRepo, generator, speaker)
        advanceUntilIdle()

        val challenge = viewModel.state.value.challenge!!
        val correctAnswer = challenge.options.first { challenge.isCorrect(it) }

        viewModel.onAnswer(correctAnswer)
        advanceUntilIdle()

        assertEquals(1, progressRepo.recordedAttempts.size)
        assertTrue(progressRepo.recordedAttempts.first().third)
    }

    @Test
    fun onAnswer_incorrect_setsTryAgainFeedback() = runTest {
        val wordRepo = FakeWordRepository()
        val progressRepo = FakeProgressRepository()
        val settingsRepo = FakeSettingsRepository()
        val generator = ChallengeGenerator()
        val speaker = FakeTtsSpeaker()

        val viewModel = BubblePopViewModel(wordRepo, progressRepo, settingsRepo, generator, speaker)
        advanceUntilIdle()

        val challenge = viewModel.state.value.challenge!!
        val wrongAnswer = challenge.options.first { !challenge.isCorrect(it) }

        viewModel.onAnswer(wrongAnswer)
        advanceUntilIdle()

        assertEquals(Feedback.TRY_AGAIN, viewModel.state.value.feedback)
    }

    @Test
    fun replaySound_speaksChallengePrompt() = runTest {
        val wordRepo = FakeWordRepository()
        val progressRepo = FakeProgressRepository()
        val settingsRepo = FakeSettingsRepository()
        val generator = ChallengeGenerator()
        val speaker = FakeTtsSpeaker()

        val viewModel = BubblePopViewModel(wordRepo, progressRepo, settingsRepo, generator, speaker)
        advanceUntilIdle()

        val initialSpokenCount = speaker.spokenTexts.size
        viewModel.replaySound()

        assertEquals(initialSpokenCount + 1, speaker.spokenTexts.size)
    }
}
