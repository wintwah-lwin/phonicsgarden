package com.example.phonicsgarden.ui.games.bubble

import com.example.phonicsgarden.data.repository.AppSettings
import com.example.phonicsgarden.engine.ChallengeGenerator
import com.example.phonicsgarden.testutil.FakeProgressRepository
import com.example.phonicsgarden.testutil.FakeSettingsRepository
import com.example.phonicsgarden.testutil.FakeSoundEffects
import com.example.phonicsgarden.testutil.FakeTtsSpeaker
import com.example.phonicsgarden.testutil.FakeWordRepository
import com.example.phonicsgarden.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BubblePopReducedMotionTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun viewModel(settings: AppSettings) = BubblePopViewModel(
        FakeWordRepository(), FakeProgressRepository(), FakeSettingsRepository(settings),
        ChallengeGenerator(), FakeTtsSpeaker(), FakeSoundEffects()
    )

    @Test
    fun bubblesFloatByDefault() = runTest {
        val vm = viewModel(AppSettings())
        advanceUntilIdle()

        assertFalse(vm.state.value.reducedMotion)
    }

    @Test
    fun reduceMotionSettingIsPassedToTheScreen() = runTest {
        val vm = viewModel(AppSettings(reducedMotion = true))
        advanceUntilIdle()

        assertTrue(vm.state.value.reducedMotion)
    }
}
