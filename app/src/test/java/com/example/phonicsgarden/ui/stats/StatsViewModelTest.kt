package com.example.phonicsgarden.ui.stats

import com.example.phonicsgarden.testutil.FakeProgressRepository
import com.example.phonicsgarden.testutil.FakeWordRepository
import com.example.phonicsgarden.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StatsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun statsState_combinesGraphemesAndAttemptsCorrectly() = runTest {
        val fakeWordRepo = FakeWordRepository()
        val fakeProgressRepo = FakeProgressRepository()

        // Record a correct attempt and an incorrect attempt
        fakeProgressRepo.recordAttempt(graphemeId = 1L, game = "bubble_pop", correct = true)
        fakeProgressRepo.recordAttempt(graphemeId = 2L, game = "bubble_pop", correct = false)

        val viewModel = StatsViewModel(fakeWordRepo, fakeProgressRepo)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }

        val state = viewModel.state.value
        assertEquals(2, state.totalAttempts)
        assertEquals(1, state.correctAttempts)
        assertEquals(fakeWordRepo.graphemesList.size, state.sounds.size)
        assertEquals(1, state.sounds.first { it.letter == "s" }.box)
    }
}
