package com.example.phonicsgarden.ui.parentgate

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNode
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.phonicsgarden.engine.MathQuestion
import com.example.phonicsgarden.ui.theme.PhonicsGardenTheme
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ParentGateDialogTest {

    @get:Rule
    val composeRule = createComposeRule()

    private var succeeded = false
    private var dismissed = false

    private fun showDialog() {
        composeRule.setContent {
            PhonicsGardenTheme {
                ParentGateDialog(
                    onSuccess = { succeeded = true },
                    onDismiss = { dismissed = true },
                    question = MathQuestion(5, 7, 12)
                )
            }
        }
    }

    @Test
    fun showsTheMathQuestion() {
        showDialog()
        composeRule.onNodeWithText("What is 5 + 7?").assertIsDisplayed()
    }

    @Test
    fun correctAnswer_unlocks() {
        showDialog()
        composeRule.onNode(hasSetTextAction()).performTextInput("12")
        composeRule.onNodeWithText("Submit").performClick()

        assertTrue(succeeded)
    }

    @Test
    fun wrongAnswer_showsErrorAndStaysLocked() {
        showDialog()
        composeRule.onNode(hasSetTextAction()).performTextInput("13")
        composeRule.onNodeWithText("Submit").performClick()

        composeRule.onNodeWithText("Incorrect answer. Please try again.").assertIsDisplayed()
        assertFalse(succeeded)
    }

    @Test
    fun cancel_dismissesWithoutUnlocking() {
        showDialog()
        composeRule.onNodeWithText("Cancel").performClick()

        assertTrue(dismissed)
        assertFalse(succeeded)
    }
}
