package com.example.phonicsgarden.ui.stats

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.phonicsgarden.engine.ActivityTimeline
import com.example.phonicsgarden.ui.theme.PhonicsGardenTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class StatsContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val state = StatsState(
        sounds = listOf(SoundProgress("s", 5), SoundProgress("a", 1)),
        totalAttempts = 10,
        correctAttempts = 7,
        last7Days = ActivityTimeline.lastDays(emptyList(), LocalDate.of(2026, 11, 27)),
        practiceNext = listOf("a")
    )

    private fun showStats(onLock: () -> Unit = {}) {
        composeRule.setContent {
            PhonicsGardenTheme { StatsContent(state = state, onLock = onLock) }
        }
    }

    @Test
    fun showsOverallAccuracy() {
        showStats()
        composeRule.onNodeWithText("10 answers so far, 70% correct").assertIsDisplayed()
    }

    @Test
    fun showsWhichSoundsToPractiseNext() {
        showStats()
        composeRule.onNodeWithText("Practise next: a").assertIsDisplayed()
    }

    @Test
    fun weeklyChartHasAnAccessibleSummary() {
        showStats()
        composeRule.onNodeWithContentDescription("Answers in the last 7 days", substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun lockButton_locksTheGate() {
        var locked = false
        showStats(onLock = { locked = true })

        composeRule.onNodeWithText("Lock Gate").performClick()

        assertTrue(locked)
    }
}
