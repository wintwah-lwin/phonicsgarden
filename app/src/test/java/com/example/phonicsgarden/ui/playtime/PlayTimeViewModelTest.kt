package com.example.phonicsgarden.ui.playtime

import com.example.phonicsgarden.engine.PlayStopwatch
import com.example.phonicsgarden.testutil.FakePlayTimeRepository
import com.example.phonicsgarden.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlayTimeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private var now = 0L
    private val repository = FakePlayTimeRepository()
    private val viewModel by lazy { PlayTimeViewModel(repository, PlayStopwatch { now }) }

    @Test
    fun timeInAGameIsAddedWhenTheChildLeaves() = runTest {
        viewModel.onGameVisible(true)
        now += 90_000
        viewModel.onGameVisible(false)

        assertEquals(listOf(90_000L), repository.added)
    }

    @Test
    fun nothingIsCountedWhenNoGameWasOpen() = runTest {
        viewModel.onGameVisible(false)

        assertTrue(repository.added.isEmpty())
    }

    @Test
    fun goingToTheBackgroundStopsTheClock() = runTest {
        viewModel.onGameVisible(true)
        now += 30_000
        viewModel.onGameVisible(false) // app goes to the background
        now += 600_000                 // ten minutes away from the screen
        viewModel.onGameVisible(false)

        assertEquals(listOf(30_000L), repository.added)
    }

    @Test
    fun comingBackToAGameStartsCountingAgain() = runTest {
        viewModel.onGameVisible(true)
        now += 10_000
        viewModel.onGameVisible(false)
        viewModel.onGameVisible(true)
        now += 20_000
        viewModel.onGameVisible(false)

        assertEquals(listOf(10_000L, 20_000L), repository.added)
    }

    @Test
    fun repeatedVisibleCallsDoNotRestartTheClock() = runTest {
        viewModel.onGameVisible(true)
        now += 5_000
        viewModel.onGameVisible(true)
        now += 5_000
        viewModel.onGameVisible(false)

        assertEquals(listOf(10_000L), repository.added)
    }
}
