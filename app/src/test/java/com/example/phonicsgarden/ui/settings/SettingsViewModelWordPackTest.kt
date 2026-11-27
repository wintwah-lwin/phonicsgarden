package com.example.phonicsgarden.ui.settings

import com.example.phonicsgarden.data.repository.WordPackSyncResult
import com.example.phonicsgarden.testutil.FakeSettingsRepository
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
class SettingsViewModelWordPackTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val wordRepo = FakeWordRepository()
    // Created lazily so it is built after MainDispatcherRule has installed the test dispatcher.
    private val viewModel by lazy { SettingsViewModel(FakeSettingsRepository(), wordRepo) }

    @Test
    fun startsIdle() {
        assertEquals(SyncStatus.Idle, viewModel.syncStatus.value)
    }

    @Test
    fun newWords_reportsHowManyWereAdded() = runTest {
        wordRepo.syncResult = WordPackSyncResult.Success(packVersion = 1, added = 5, rejected = 0)

        viewModel.checkForNewWords()

        assertEquals(SyncStatus.Updated(5), viewModel.syncStatus.value)
    }

    @Test
    fun noNewWords_reportsUpToDate() = runTest {
        wordRepo.syncResult = WordPackSyncResult.Success(packVersion = 1, added = 0, rejected = 2)

        viewModel.checkForNewWords()

        assertEquals(SyncStatus.UpToDate, viewModel.syncStatus.value)
    }

    @Test
    fun noConnection_reportsOfflineWithoutCrashing() = runTest {
        wordRepo.syncResult = WordPackSyncResult.Failed

        viewModel.checkForNewWords()

        assertEquals(SyncStatus.Offline, viewModel.syncStatus.value)
    }

    @Test
    fun downloadedWordCount_followsTheRepository() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.downloadedWords.collect {}
        }

        wordRepo.downloadedWordCount.value = 12

        assertEquals(12, viewModel.downloadedWords.value)
    }
}
