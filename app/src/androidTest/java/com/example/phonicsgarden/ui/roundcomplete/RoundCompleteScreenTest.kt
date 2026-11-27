package com.example.phonicsgarden.ui.roundcomplete

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.phonicsgarden.ui.theme.PhonicsGardenTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoundCompleteScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun showsTheScoreAndABreakSuggestion() {
        composeRule.setContent {
            PhonicsGardenTheme { RoundCompleteScreen(correct = 4, total = 6, onDone = {}) }
        }

        composeRule.onNodeWithText("You got 4 of 6 on the first try.").assertIsDisplayed()
        composeRule.onNodeWithText("Time for a little break, or play again soon.").assertIsDisplayed()
    }

    @Test
    fun backButton_returnsToTheGarden() {
        var done = false
        composeRule.setContent {
            PhonicsGardenTheme { RoundCompleteScreen(correct = 6, total = 6, onDone = { done = true }) }
        }

        composeRule.onNodeWithText("Back to the garden").performClick()
        assertTrue(done)
    }
}
