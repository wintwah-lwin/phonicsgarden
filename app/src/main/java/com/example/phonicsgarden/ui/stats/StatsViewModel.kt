package com.example.phonicsgarden.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonicsgarden.data.repository.ProgressRepository
import com.example.phonicsgarden.data.repository.WordRepository
import com.example.phonicsgarden.engine.DayActivity
import com.example.phonicsgarden.engine.MasteryScheduler
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
    val correctAttempts: Int = 0,
    val last7Days: List<DayActivity> = emptyList(),
    /** The sounds a grown-up could help with next (least known first). */
    val practiceNext: List<String> = emptyList()
)

@HiltViewModel
class StatsViewModel @Inject constructor(
    wordRepository: WordRepository,
    progressRepository: ProgressRepository
) : ViewModel() {

    val state: StateFlow<StatsState> = combine(
        wordRepository.observeGraphemes(),
        progressRepository.observeMasteryBoxes(),
        progressRepository.observeAttemptSummary(),
        progressRepository.observeLastSevenDays()
    ) { graphemes, boxes, summary, lastSevenDays ->
        val sounds = graphemes.map { SoundProgress(it.letter, boxes[it.id] ?: 0) }
        StatsState(
            sounds = sounds,
            totalAttempts = summary.total,
            correctAttempts = summary.correct,
            last7Days = lastSevenDays,
            practiceNext = sounds
                .filter { !MasteryScheduler.isMastered(it.box) }
                .sortedBy { it.box } // stable, so sounds in the same box keep teaching order
                .take(PRACTICE_NEXT_COUNT)
                .map { it.letter }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), StatsState())

    private companion object {
        const val PRACTICE_NEXT_COUNT = 3
    }
}
