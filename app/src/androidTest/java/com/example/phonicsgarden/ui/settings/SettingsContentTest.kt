package com.example.phonicsgarden.ui.settings

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodes
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.phonicsgarden.data.repository.AppSettings
import com.example.phonicsgarden.ui.theme.PhonicsGardenTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    private var selectedPhase: Int? = null
    private var soundValue: Boolean? = null
    private var motionValue: Boolean? = null
    private var locked = false
    private var checkRequested = false

    private fun showSettings(
        settings: AppSettings = AppSettings(),
        downloadedWords: Int = 0,
        syncStatus: SyncStatus = SyncStatus.Idle
    ) {
        composeRule.setContent {
            PhonicsGardenTheme {
                SettingsContent(
                    settings = settings,
                    onPhaseSelected = { selectedPhase = it },
                    onSoundChanged = { soundValue = it },
                    onReducedMotionChanged = { motionValue = it },
                    onLock = { locked = true },
                    downloadedWords = downloadedWords,
                    syncStatus = syncStatus,
                    onCheckForWords = { checkRequested = true }
                )
            }
        }
    }

    @Test
    fun selectingAPhase_reportsIt() {
        showSettings()
        composeRule.onNodeWithText("Phase 2").performClick()
        assertEquals(2, selectedPhase)
    }

    @Test
    fun descriptionMatchesTheSelectedPhase() {
        showSettings(AppSettings(phase = 2))
        composeRule.onNodeWithText("Phase 2: adds m d g o c k").assertIsDisplayed()
    }

    @Test
    fun soundSwitch_reportsTheNewValue() {
        showSettings(AppSettings(soundEnabled = true))
        composeRule.onAllNodes(isToggleable())[0].performClick()
        assertEquals(false, soundValue)
    }

    @Test
    fun reduceMotionSwitch_reportsTheNewValue() {
        showSettings(AppSettings(reducedMotion = false))
        composeRule.onAllNodes(isToggleable())[1].performClick()
        assertEquals(true, motionValue)
    }

    @Test
    fun checkButton_asksForNewWords() {
        showSettings()
        composeRule.onNodeWithText("Check for new words").performScrollTo().performClick()
        assertTrue(checkRequested)
    }

    @Test
    fun showsDownloadedWordCountAndResult() {
        showSettings(downloadedWords = 12, syncStatus = SyncStatus.Updated(3))
        composeRule.onNodeWithText("12 extra words downloaded").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Added 3 new words!").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun offlineMessageIsFriendly() {
        showSettings(syncStatus = SyncStatus.Offline)
        composeRule.onNodeWithText("Couldn't reach the word list. The built-in words still work.")
            .performScrollTo().assertIsDisplayed()
    }

    @Test
    fun lockButton_locksTheGate() {
        showSettings()
        composeRule.onNodeWithText("Lock Gate").performClick()
        assertTrue(locked)
    }
}
