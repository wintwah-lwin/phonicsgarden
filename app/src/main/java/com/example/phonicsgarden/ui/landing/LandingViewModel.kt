package com.example.phonicsgarden.ui.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonicsgarden.data.repository.PlayTimeRepository
import com.example.phonicsgarden.data.repository.ProgressRepository
import com.example.phonicsgarden.data.repository.SettingsRepository
import com.example.phonicsgarden.data.repository.WordRepository
import com.example.phonicsgarden.engine.MasteryScheduler
import com.example.phonicsgarden.engine.PlayLimit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class GardenState(val mastered: Int = 0, val total: Int = 0)

@HiltViewModel
class LandingViewModel @Inject constructor(
    wordRepository: WordRepository,
    progressRepository: ProgressRepository,
    settingsRepository: SettingsRepository,
    playTimeRepository: PlayTimeRepository
) : ViewModel() {

    /** True once today's play time has reached the grown-up's limit. */
    val playTimeUp: StateFlow<Boolean> = combine(
        settingsRepository.settings,
        playTimeRepository.observePlayedTodayMillis()
    ) { settings, played ->
        PlayLimit.isReached(played, settings.dailyLimitMinutes)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val garden: StateFlow<GardenState> = combine(
        wordRepository.observeGraphemes(),
        progressRepository.observeMasteryBoxes()
    ) { graphemes, boxes ->
        val mastered = graphemes.count { MasteryScheduler.isMastered(boxes[it.id] ?: 0) }
        GardenState(mastered, graphemes.size)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), GardenState())
}
