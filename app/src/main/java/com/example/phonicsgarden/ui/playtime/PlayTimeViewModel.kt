package com.example.phonicsgarden.ui.playtime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonicsgarden.data.repository.PlayTimeRepository
import com.example.phonicsgarden.engine.PlayStopwatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Counts the time a game is on screen and adds it to today's total. It lives as long as the
 * activity, so time is counted across screens. Nothing is shown to the child while it runs.
 */
@HiltViewModel
class PlayTimeViewModel internal constructor(
    private val repository: PlayTimeRepository,
    private val stopwatch: PlayStopwatch
) : ViewModel() {

    @Inject
    constructor(repository: PlayTimeRepository) : this(repository, PlayStopwatch())

    /**
     * Call with true when a game screen is showing, and false when the child leaves the game or
     * the app goes to the background (so time away from the screen is never counted).
     */
    fun onGameVisible(visible: Boolean) {
        if (visible) {
            stopwatch.start()
        } else {
            val elapsed = stopwatch.stop()
            if (elapsed > 0L) viewModelScope.launch { repository.addPlayTime(elapsed) }
        }
    }
}
