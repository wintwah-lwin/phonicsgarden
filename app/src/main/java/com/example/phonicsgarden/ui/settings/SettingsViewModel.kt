package com.example.phonicsgarden.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonicsgarden.data.repository.AppSettings
import com.example.phonicsgarden.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {

    val settings: StateFlow<AppSettings> = repository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AppSettings())

    fun setPhase(phase: Int) = viewModelScope.launch { repository.setPhase(phase) }
    fun setSoundEnabled(enabled: Boolean) = viewModelScope.launch { repository.setSoundEnabled(enabled) }
    fun setReducedMotion(enabled: Boolean) = viewModelScope.launch { repository.setReducedMotion(enabled) }
}
