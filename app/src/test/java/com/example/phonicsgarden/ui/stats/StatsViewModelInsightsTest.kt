package com.example.phonicsgarden.ui.stats

import com.example.phonicsgarden.engine.ActivityTimeline
import com.example.phonicsgarden.engine.MasteryScheduler
import com.example.phonicsgarden.testutil.FakeProgressRepository
import com.example.phonicsgarden.testutil.FakeWordRepository
import com.example.phonicsgarden.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class StatsViewModelInsightsTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val progressRepo = FakeProgressRepository()

    private fun TestScope.startViewModel(): StatsViewModel {
        val viewModel = StatsViewModel(FakeWordRepository(), progressRepo)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect {} }
        return viewModel
    }

    @Test
    fun practiceNext_listsTheThreeLeastKnownSoundsInTeachingOrder() = runTest {
        progressRepo.setMasteryBox(graphemeId = 1L, box = MasteryScheduler.MAX_BOX) // s: mastered
        progressRepo.setMasteryBox(graphemeId = 2L, box = 3)                        // a: getting there

        val state = startViewModel().state.value

        // s is mastered and a is further along than the untouched sounds, so t, p, i come first.
        assertEquals(listOf("t", "p", "i"), state.practiceNext)
    }

    @Test
    fun practiceNext_isEmptyWhenEverythingIsMastered() = runTest {
        FakeWordRepository().graphemesList.forEach {
            progressRepo.setMasteryBox(it.id, MasteryScheduler.MAX_BOX)
        }

        assertTrue(startViewModel().state.value.practiceNext.isEmpty())
    }

    @Test
    fun lastSevenDays_comesFromTheRepository() = runTest {
        val week = ActivityTimeline.lastDays(emptyList(), LocalDate.of(2026, 11, 27))
        progressRepo.setLastSevenDays(week)

        assertEquals(week, startViewModel().state.value.last7Days)
    }
}
