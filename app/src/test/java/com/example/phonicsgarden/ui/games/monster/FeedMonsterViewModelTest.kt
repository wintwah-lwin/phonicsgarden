package com.example.phonicsgarden.ui.games.monster

import com.example.phonicsgarden.engine.WordChallengeGenerator
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
class FeedMonsterViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun startRound_initializesChallenges() = runTest {
        val wordRepo = FakeWordRepository()
        val progressRepo = FakeProgressRepository()
        val settingsRepo = FakeSettingsRepository()
        val generator = WordChallengeGenerator()
        val speaker = FakeTtsSpeaker()

        val viewModel = FeedMonsterViewModel(wordRepo, progressRepo, settingsRepo, generator, speaker)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertNotNull(state.challenge)
        assertEquals(FeedMonsterViewModel.ROUND_LENGTH, state.total)
        assertEquals(1, state.questionNumber)
    }

    @Test
    fun onFed_correctWord_recordsAttemptAndSetsCorrectFeedback() = runTest {
        val wordRepo = FakeWordRepository()
        val progressRepo = FakeProgressRepository()
        val settingsRepo = FakeSettingsRepository()
        val generator = WordChallengeGenerator()
        val speaker = FakeTtsSpeaker()

        val viewModel = FeedMonsterViewModel(wordRepo, progressRepo, settingsRepo, generator, speaker)
        advanceUntilIdle()

        val challenge = viewModel.state.value.challenge!!
        val correctWord = challenge.options.first { challenge.isCorrect(it) }

        viewModel.onFed(correctWord)

        // Straight after feeding: success feedback, attempt saved, monster says the word.
        assertEquals(Feedback.CORRECT, viewModel.state.value.feedback)
        assertEquals(1, progressRepo.recordedAttempts.size)
        assertTrue(progressRepo.recordedAttempts.first().third)
        assertEquals(correctWord, speaker.spokenTexts.last())

        // After the short celebration delay the next question appears.
        advanceUntilIdle()
        assertEquals(2, viewModel.state.value.questionNumber)
    }

    @Test
    fun onFed_wrongWord_setsTryAgainFeedback() = runTest {
        val wordRepo = FakeWordRepository()
        val progressRepo = FakeProgressRepository()
        val settingsRepo = FakeSettingsRepository()
        val generator = WordChallengeGenerator()
        val speaker = FakeTtsSpeaker()

        val viewModel = FeedMonsterViewModel(wordRepo, progressRepo, settingsRepo, generator, speaker)
        advanceUntilIdle()

        val challenge = viewModel.state.value.challenge!!
        val wrongWord = challenge.options.first { !challenge.isCorrect(it) }

        viewModel.onFed(wrongWord)
        advanceUntilIdle()

        assertEquals(Feedback.TRY_AGAIN, viewModel.state.value.feedback)
    }

    @Test
    fun replayPrompt_speaksChallengePrompt() = runTest {
        val wordRepo = FakeWordRepository()
        val progressRepo = FakeProgressRepository()
        val settingsRepo = FakeSettingsRepository()
        val generator = WordChallengeGenerator()
        val speaker = FakeTtsSpeaker()

        val viewModel = FeedMonsterViewModel(wordRepo, progressRepo, settingsRepo, generator, speaker)
        advanceUntilIdle()

        val initialSpokenCount = speaker.spokenTexts.size
        viewModel.replayPrompt()

        assertEquals(initialSpokenCount + 1, speaker.spokenTexts.size)
    }
}
