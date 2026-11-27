package com.example.phonicsgarden.ui.landing

import com.example.phonicsgarden.engine.MasteryScheduler
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
class LandingViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun gardenState_calculatesMasteredGraphemesCorrectly() = runTest {
        val fakeWordRepo = FakeWordRepository()
        val fakeProgressRepo = FakeProgressRepository()

        // Set grapheme 1 to MAX_BOX (mastered)
        fakeProgressRepo.setMasteryBox(graphemeId = 1L, box = MasteryScheduler.MAX_BOX)

        val viewModel = LandingViewModel(fakeWordRepo, fakeProgressRepo)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.garden.collect {}
        }

        val state = viewModel.garden.value
        assertEquals(1, state.mastered)
        assertEquals(fakeWordRepo.graphemesList.size, state.total)
    }
}
