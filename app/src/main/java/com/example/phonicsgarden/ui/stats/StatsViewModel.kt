package com.example.phonicsgarden.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonicsgarden.data.repository.ProgressRepository
import com.example.phonicsgarden.data.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class SoundProgress(val letter: String, val box: Int)

data class StatsState(
    val sounds: List<SoundProgress> = emptyList(),
    val totalAttempts: Int = 0,
    val correctAttempts: Int = 0
)

@HiltViewModel
class StatsViewModel @Inject constructor(
    wordRepository: WordRepository,
    progressRepository: ProgressRepository
) : ViewModel() {

    val state: StateFlow<StatsState> = combine(
        wordRepository.observeGraphemes(),
        progressRepository.observeMasteryBoxes(),
        progressRepository.observeAttemptSummary()
    ) { graphemes, boxes, summary ->
        StatsState(
            sounds = graphemes.map { SoundProgress(it.letter, boxes[it.id] ?: 0) },
            totalAttempts = summary.total,
            correctAttempts = summary.correct
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), StatsState())
}
