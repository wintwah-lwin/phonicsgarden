package com.example.phonicsgarden.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonicsgarden.data.repository.AppSettings
import com.example.phonicsgarden.data.repository.SettingsRepository
import com.example.phonicsgarden.data.repository.WordPackSyncResult
import com.example.phonicsgarden.data.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/** What happened the last time the parent asked to check for new words. */
sealed interface SyncStatus {
    data object Idle : SyncStatus
    data object Checking : SyncStatus
    data class Updated(val added: Int) : SyncStatus
    data object UpToDate : SyncStatus
    data object Offline : SyncStatus
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository,
    private val wordRepository: WordRepository
) : ViewModel() {

    val settings: StateFlow<AppSettings> = repository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AppSettings())

    val downloadedWords: StateFlow<Int> = wordRepository.observeDownloadedWordCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    private val _syncStatus = MutableStateFlow<SyncStatus>(SyncStatus.Idle)
    val syncStatus: StateFlow<SyncStatus> = _syncStatus.asStateFlow()

    fun setPhase(phase: Int) = viewModelScope.launch { repository.setPhase(phase) }
    fun setSoundEnabled(enabled: Boolean) = viewModelScope.launch { repository.setSoundEnabled(enabled) }
    fun setReducedMotion(enabled: Boolean) = viewModelScope.launch { repository.setReducedMotion(enabled) }

    /** Downloads the latest word pack (only when the parent asks, plus once at app start). */
    fun checkForNewWords() {
        if (_syncStatus.value == SyncStatus.Checking) return
        viewModelScope.launch {
            _syncStatus.value = SyncStatus.Checking
            _syncStatus.value = when (val result = wordRepository.syncWordPack()) {
                is WordPackSyncResult.Success ->
                    if (result.added > 0) SyncStatus.Updated(result.added) else SyncStatus.UpToDate
                WordPackSyncResult.Failed -> SyncStatus.Offline
            }
        }
    }
}
