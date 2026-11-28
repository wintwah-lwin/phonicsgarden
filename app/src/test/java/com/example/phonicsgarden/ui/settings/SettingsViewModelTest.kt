package com.example.phonicsgarden.ui.settings

import com.example.phonicsgarden.data.repository.AppSettings
import com.example.phonicsgarden.data.repository.SettingsRepository
import com.example.phonicsgarden.testutil.FakeWordRepository
import com.example.phonicsgarden.testutil.FakePlayTimeRepository
import com.example.phonicsgarden.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val settingsFlow = MutableStateFlow(AppSettings())
    private var lastPhaseSet: Int? = null
    private var lastSoundSet: Boolean? = null
    private var lastMotionSet: Boolean? = null

    private val fakeRepository = object : SettingsRepository {
        override val settings = settingsFlow
        override suspend fun setPhase(phase: Int) {
            lastPhaseSet = phase
            settingsFlow.value = settingsFlow.value.copy(phase = phase)
        }
        override suspend fun setSoundEnabled(enabled: Boolean) {
            lastSoundSet = enabled
            settingsFlow.value = settingsFlow.value.copy(soundEnabled = enabled)
        }
        override suspend fun setReducedMotion(enabled: Boolean) {
            lastMotionSet = enabled
            settingsFlow.value = settingsFlow.value.copy(reducedMotion = enabled)
        }

        override suspend fun setDailyLimitMinutes(minutes: Int) {
            settingsFlow.value = settingsFlow.value.copy(dailyLimitMinutes = minutes)
        }
    }

    @Test
    fun setPhase_updatesRepositoryAndState() = runTest {
        val viewModel = SettingsViewModel(fakeRepository, FakeWordRepository(), FakePlayTimeRepository())
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.settings.collect {}
        }

        viewModel.setPhase(2)

        assertEquals(2, lastPhaseSet)
        assertEquals(2, viewModel.settings.value.phase)
    }

    @Test
    fun setSoundEnabled_updatesRepositoryAndState() = runTest {
        val viewModel = SettingsViewModel(fakeRepository, FakeWordRepository(), FakePlayTimeRepository())
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.settings.collect {}
        }

        viewModel.setSoundEnabled(false)

        assertEquals(false, lastSoundSet)
        assertFalse(viewModel.settings.value.soundEnabled)
    }

    @Test
    fun setReducedMotion_updatesRepositoryAndState() = runTest {
        val viewModel = SettingsViewModel(fakeRepository, FakeWordRepository(), FakePlayTimeRepository())
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.settings.collect {}
        }

        viewModel.setReducedMotion(true)

        assertEquals(true, lastMotionSet)
        assertTrue(viewModel.settings.value.reducedMotion)
    }
}
