package com.example.phonicsgarden.engine

data class MasteryState(val box: Int, val nextReviewAt: Long)

/**
 * Simple Leitner-style scheduler. A correct answer moves a sound up one box and delays its
 * next review; a wrong answer drops it one box (gentle for young children, rather than
 * resetting to zero).
 */
object MasteryScheduler {
    const val MAX_BOX = 5
    const val MASTERED_BOX = 4

    private const val DAY_MS = 24L * 60 * 60 * 1000
    private val reviewDelayDays = intArrayOf(0, 1, 2, 4, 7, 14)

    fun next(current: MasteryState, correct: Boolean, now: Long): MasteryState {
        val box = if (correct) {
            (current.box + 1).coerceAtMost(MAX_BOX)
        } else {
            (current.box - 1).coerceAtLeast(0)
        }
        return MasteryState(box, now + reviewDelayDays[box] * DAY_MS)
    }

    fun isMastered(box: Int): Boolean = box >= MASTERED_BOX

    fun isDue(state: MasteryState, now: Long): Boolean = state.nextReviewAt <= now
}
