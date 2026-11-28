package com.example.phonicsgarden.ui.privacy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonicsgarden.data.repository.PlayTimeRepository
import com.example.phonicsgarden.data.repository.ProgressRepository
import com.example.phonicsgarden.data.repository.WordRepository
import com.example.phonicsgarden.engine.PlayLimit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Live counts of what the app keeps on the device, so the "What we store" screen is truthful. */
data class PrivacyState(
    val answersStored: Int = 0,
    val soundsPractised: Int = 0,
    val downloadedWords: Int = 0,
    val playedTodayMinutes: Int = 0
)

@HiltViewModel
class PrivacyViewModel @Inject constructor(
    private val progressRepository: ProgressRepository,
    wordRepository: WordRepository,
    private val playTimeRepository: PlayTimeRepository
) : ViewModel() {

    val state: StateFlow<PrivacyState> = combine(
        progressRepository.observeAttemptSummary(),
        progressRepository.observeMasteryBoxes(),
        wordRepository.observeDownloadedWordCount(),
        playTimeRepository.observePlayedTodayMillis()
    ) { summary, boxes, words, played ->
        PrivacyState(
            answersStored = summary.total,
            soundsPractised = boxes.size,
            downloadedWords = words,
            playedTodayMinutes = PlayLimit.minutesPlayed(played)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PrivacyState())

    /** Erases the child's answers, sound levels and today's play time. Settings are kept. */
    fun deleteAllProgress() {
        viewModelScope.launch {
            progressRepository.deleteAllProgress()
            playTimeRepository.resetToday()
        }
    }
}
