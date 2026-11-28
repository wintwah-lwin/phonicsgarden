package com.example.phonicsgarden.ui.roundcomplete

import com.example.phonicsgarden.data.repository.AppSettings
import com.example.phonicsgarden.testutil.FakePlayTimeRepository
import com.example.phonicsgarden.testutil.FakeSettingsRepository
import com.example.phonicsgarden.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RoundCompleteViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val playTime = FakePlayTimeRepository()

    private fun TestScope.start(settings: AppSettings): RoundCompleteViewModel {
        val viewModel = RoundCompleteViewModel(FakeSettingsRepository(settings), playTime)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.limitReached.collect {} }
        return viewModel
    }

    @Test
    fun noLimit_isNeverReached() = runTest {
        playTime.setPlayedMillis(3_600_000)
        assertFalse(start(AppSettings(dailyLimitMinutes = 0)).limitReached.value)
    }

    @Test
    fun belowTheLimit_isNotReached() = runTest {
        playTime.setPlayedMillis(5 * 60_000L)
        assertFalse(start(AppSettings(dailyLimitMinutes = 10)).limitReached.value)
    }

    @Test
    fun atTheLimit_isReached() = runTest {
        playTime.setPlayedMillis(10 * 60_000L)
        assertTrue(start(AppSettings(dailyLimitMinutes = 10)).limitReached.value)
    }

    @Test
    fun becomesReachedAsTimeIsAdded() = runTest {
        val viewModel = start(AppSettings(dailyLimitMinutes = 10))
        assertFalse(viewModel.limitReached.value)

        playTime.addPlayTime(11 * 60_000L)

        assertTrue(viewModel.limitReached.value)
    }
}
