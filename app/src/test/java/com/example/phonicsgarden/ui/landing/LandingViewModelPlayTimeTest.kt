package com.example.phonicsgarden.ui.landing

import com.example.phonicsgarden.data.repository.AppSettings
import com.example.phonicsgarden.testutil.FakePlayTimeRepository
import com.example.phonicsgarden.testutil.FakeProgressRepository
import com.example.phonicsgarden.testutil.FakeSettingsRepository
import com.example.phonicsgarden.testutil.FakeWordRepository
import com.example.phonicsgarden.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LandingViewModelPlayTimeTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val playTime = FakePlayTimeRepository()

    private fun TestScope.start(settings: AppSettings): LandingViewModel {
        val viewModel = LandingViewModel(
            FakeWordRepository(), FakeProgressRepository(), FakeSettingsRepository(settings), playTime
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.playTimeUp.collect {} }
        return viewModel
    }

    @Test
    fun gamesStayOpenWhenThereIsNoLimit() = runTest {
        playTime.setPlayedMillis(2 * 3_600_000L)
        assertFalse(start(AppSettings(dailyLimitMinutes = 0)).playTimeUp.value)
    }

    @Test
    fun gamesRestOnceTheLimitIsUsedUp() = runTest {
        playTime.setPlayedMillis(10 * 60_000L)
        assertTrue(start(AppSettings(dailyLimitMinutes = 10)).playTimeUp.value)
    }

    @Test
    fun aParentRaisingTheLimitReopensTheGames() = runTest {
        playTime.setPlayedMillis(10 * 60_000L)
        val settings = FakeSettingsRepository(AppSettings(dailyLimitMinutes = 10))
        val viewModel = LandingViewModel(FakeWordRepository(), FakeProgressRepository(), settings, playTime)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.playTimeUp.collect {} }
        assertTrue(viewModel.playTimeUp.value)

        settings.setDailyLimitMinutes(20)

        assertFalse(viewModel.playTimeUp.value)
    }
}
