package com.example.phonicsgarden.ui.landing

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.phonicsgarden.ui.theme.PhonicsGardenTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LandingContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    private var bubbleClicked = false
    private var monsterClicked = false
    private var tracingClicked = false

    private fun showLanding(garden: GardenState = GardenState(mastered = 2, total = 6)) {
        composeRule.setContent {
            PhonicsGardenTheme {
                LandingContent(
                    garden = garden,
                    onBubblePop = { bubbleClicked = true },
                    onFeedMonster = { monsterClicked = true },
                    onTracing = { tracingClicked = true }
                )
            }
        }
    }

    @Test
    fun showsAllThreeGames() {
        showLanding()
        listOf("Bubble Pop", "Feed the Monster", "Tracing Garden").forEach { title ->
            composeRule.onNodeWithText(title).performScrollTo().assertIsDisplayed()
        }
    }

    @Test
    fun gardenCardShowsProgress() {
        showLanding(GardenState(mastered = 2, total = 6))
        composeRule.onNodeWithText("2 of 6 sounds grown").assertIsDisplayed()
    }

    @Test
    fun tappingEachTileOpensThatGame() {
        showLanding()

        composeRule.onNodeWithText("Bubble Pop").performScrollTo().performClick()
        composeRule.onNodeWithText("Feed the Monster").performScrollTo().performClick()
        composeRule.onNodeWithText("Tracing Garden").performScrollTo().performClick()

        assertTrue(bubbleClicked)
        assertTrue(monsterClicked)
        assertTrue(tracingClicked)
    }
}
