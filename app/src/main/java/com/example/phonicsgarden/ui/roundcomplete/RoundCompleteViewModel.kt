package com.example.phonicsgarden.ui.roundcomplete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonicsgarden.data.repository.PlayTimeRepository
import com.example.phonicsgarden.data.repository.SettingsRepository
import com.example.phonicsgarden.engine.PlayLimit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RoundCompleteViewModel @Inject constructor(
    settingsRepository: SettingsRepository,
    playTimeRepository: PlayTimeRepository
) : ViewModel() {

    /** True once today's play time has reached the limit the grown-up set. */
    val limitReached: StateFlow<Boolean> = combine(
        settingsRepository.settings,
        playTimeRepository.observePlayedTodayMillis()
    ) { settings, played ->
        PlayLimit.isReached(played, settings.dailyLimitMinutes)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)
}
