package com.example.phonicsgarden.ui.games.bubble

import androidx.compose.ui.test.assertExists
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.phonicsgarden.engine.Challenge
import com.example.phonicsgarden.ui.games.Feedback
import com.example.phonicsgarden.ui.theme.PhonicsGardenTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BubblePopContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val challenge = Challenge(
        prompt = "sss",
        correctAnswer = "s",
        options = listOf("s", "a", "t", "p"),
        graphemeId = 1L
    )

    private fun show(
        state: BubblePopState = BubblePopState(challenge = challenge, questionNumber = 1, total = 6),
        floating: Boolean,
        onReplay: () -> Unit = {},
        onAnswer: (String) -> Unit = {}
    ) {
        composeRule.setContent {
            PhonicsGardenTheme {
                BubblePopContent(state, floating, onReplaySound = onReplay, onAnswer = onAnswer)
            }
        }
    }

    @Test
    fun stillBubbles_showEveryLetterAndReportTaps() {
        var tapped: String? = null
        show(floating = false, onAnswer = { tapped = it })

        listOf("s", "a", "t", "p").forEach { composeRule.onNodeWithText(it).assertIsDisplayed() }
        composeRule.onNodeWithText("t").performClick()

        assertEquals("t", tapped)
    }

    @Test
    fun floatingBubbles_rendersEveryLetter() {
        // The bubbles drift forever, so pause the test clock instead of waiting for them to settle.
        composeRule.mainClock.autoAdvance = false
        show(floating = true)

        listOf("s", "a", "t", "p").forEach { composeRule.onNodeWithText(it).assertExists() }
    }

    @Test
    fun showsProgressAndTheInstruction() {
        show(floating = false)

        composeRule.onNodeWithText("1 of 6").assertIsDisplayed()
        composeRule.onNodeWithText("Pop the letter you hear!").assertIsDisplayed()
    }

    @Test
    fun feedbackMessagesAreGentle() {
        show(
            state = BubblePopState(challenge = challenge, questionNumber = 1, total = 6, feedback = Feedback.TRY_AGAIN),
            floating = false
        )
        composeRule.onNodeWithText("Try again! 💛").assertIsDisplayed()
    }

    @Test
    fun correctFeedbackIsCelebratory() {
        show(
            state = BubblePopState(challenge = challenge, questionNumber = 1, total = 6, feedback = Feedback.CORRECT),
            floating = false
        )
        composeRule.onNodeWithText("Great job! 🌟").assertIsDisplayed()
    }

    @Test
    fun hearItAgain_replaysTheSound() {
        var replayed = false
        show(floating = false, onReplay = { replayed = true })

        composeRule.onNodeWithText("🔊 Hear it again").performClick()

        assertTrue(replayed)
    }
}
