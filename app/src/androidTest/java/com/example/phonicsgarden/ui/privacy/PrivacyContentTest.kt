package com.example.phonicsgarden.ui.privacy

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.phonicsgarden.ui.theme.PhonicsGardenTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PrivacyContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    private var deletes = 0
    private var wentBack = false

    private val state = PrivacyState(
        answersStored = 42,
        soundsPractised = 5,
        downloadedWords = 12,
        playedTodayMinutes = 7
    )

    private fun show(state: PrivacyState = this.state) {
        composeRule.setContent {
            PhonicsGardenTheme {
                PrivacyContent(
                    state = state,
                    onDeleteConfirmed = { deletes++ },
                    onBack = { wentBack = true }
                )
            }
        }
    }

    @Test
    fun showsTheRealCountsOfWhatIsStored() {
        show()

        composeRule.onNodeWithText("42 answers", substring = true).assertIsDisplayed()
        composeRule.onNodeWithText("5 sounds practised", substring = true).assertIsDisplayed()
        composeRule.onNodeWithText("12 extra words", substring = true).performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("7 minutes played today", substring = true).performScrollTo().assertIsDisplayed()
    }

    @Test
    fun explainsWhatLeavesTheDeviceAndWhatIsNeverDone() {
        show()

        composeRule.onNodeWithText("What leaves this device").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("• No ads").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("• No analytics or tracking").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun deleting_asksForConfirmationFirst() {
        show()

        composeRule.onNodeWithText("Delete all progress").performScrollTo().performClick()

        composeRule.onNodeWithText("Delete all progress?").assertIsDisplayed()
        assertEquals(0, deletes)
    }

    @Test
    fun cancelling_deletesNothing() {
        show()
        composeRule.onNodeWithText("Delete all progress").performScrollTo().performClick()

        composeRule.onNodeWithText("Cancel").performClick()

        assertEquals(0, deletes)
    }

    @Test
    fun confirming_deletesOnceAndSaysSo() {
        show()
        composeRule.onNodeWithText("Delete all progress").performScrollTo().performClick()

        composeRule.onNodeWithText("Delete").performClick()

        assertEquals(1, deletes)
        composeRule.onNodeWithText("Progress deleted.").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun backButton_goesBack() {
        show()
        composeRule.onNodeWithText("Back").performClick()
        assertEquals(true, wentBack)
    }
}
