package com.example.phonicsgarden.ui.settings

import com.example.phonicsgarden.testutil.FakePlayTimeRepository
import com.example.phonicsgarden.testutil.FakeSettingsRepository
import com.example.phonicsgarden.testutil.FakeWordRepository
import com.example.phonicsgarden.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelPlayTimeTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val playTime = FakePlayTimeRepository()

    private fun TestScope.start(): SettingsViewModel {
        val viewModel = SettingsViewModel(FakeSettingsRepository(), FakeWordRepository(), playTime)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.settings.collect {}
        }
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.playedTodayMinutes.collect {}
        }
        return viewModel
    }

    @Test
    fun dailyLimitIsOffByDefault() = runTest {
        assertEquals(0, start().settings.value.dailyLimitMinutes)
    }

    @Test
    fun choosingALimitIsSaved() = runTest {
        val viewModel = start()

        viewModel.setDailyLimitMinutes(15)

        assertEquals(15, viewModel.settings.value.dailyLimitMinutes)
    }

    @Test
    fun playedTodayIsShownInWholeMinutes() = runTest {
        val viewModel = start()

        playTime.setPlayedMillis(4 * 60_000L + 45_000)

        assertEquals(4, viewModel.playedTodayMinutes.value)
    }
}
