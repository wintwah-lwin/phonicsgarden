package com.example.phonicsgarden.ui.privacy

import com.example.phonicsgarden.testutil.FakePlayTimeRepository
import com.example.phonicsgarden.testutil.FakeProgressRepository
import com.example.phonicsgarden.testutil.FakeWordRepository
import com.example.phonicsgarden.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PrivacyViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val progress = FakeProgressRepository()
    private val words = FakeWordRepository()
    private val playTime = FakePlayTimeRepository()

    private fun TestScope.start(): PrivacyViewModel {
        val viewModel = PrivacyViewModel(progress, words, playTime)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect {} }
        return viewModel
    }

    @Test
    fun startsWithNothingStored() = runTest {
        assertEquals(PrivacyState(), start().state.value)
    }

    @Test
    fun showsTheRealCountsOfWhatIsStored() = runTest {
        val viewModel = start()

        progress.recordAttempt(graphemeId = 1, game = "bubble_pop", correct = true)
        progress.recordAttempt(graphemeId = 1, game = "bubble_pop", correct = false)
        progress.recordAttempt(graphemeId = 2, game = "tracing", correct = true)
        words.downloadedWordCount.value = 12
        playTime.setPlayedMillis(7 * 60_000L + 20_000)

        val state = viewModel.state.value
        assertEquals(3, state.answersStored)
        assertEquals(2, state.soundsPractised) // sounds 1 and 2 have a level
        assertEquals(12, state.downloadedWords)
        assertEquals(7, state.playedTodayMinutes)
    }

    @Test
    fun deletingProgressErasesAnswersLevelsAndPlayTime() = runTest {
        val viewModel = start()
        progress.recordAttempt(graphemeId = 1, game = "bubble_pop", correct = true)
        playTime.setPlayedMillis(5 * 60_000L)

        viewModel.deleteAllProgress()

        val state = viewModel.state.value
        assertEquals(0, state.answersStored)
        assertEquals(0, state.soundsPractised)
        assertEquals(0, state.playedTodayMinutes)
        assertTrue(progress.recordedAttempts.isEmpty())
    }

    @Test
    fun deletingProgressKeepsDownloadedWords() = runTest {
        val viewModel = start()
        words.downloadedWordCount.value = 12

        viewModel.deleteAllProgress()

        assertEquals(12, viewModel.state.value.downloadedWords)
    }
}
