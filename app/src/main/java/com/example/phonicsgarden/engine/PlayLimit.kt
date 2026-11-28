package com.example.phonicsgarden.engine

/** Rules for the grown-up's daily play time limit. */
object PlayLimit {
    /** Choices offered to parents, in minutes per day. 0 means no limit. */
    val OPTIONS_MINUTES = listOf(0, 10, 15, 20, 30)

    private const val MILLIS_PER_MINUTE = 60_000L

    fun isReached(playedMillis: Long, limitMinutes: Int): Boolean =
        limitMinutes > 0 && playedMillis >= limitMinutes * MILLIS_PER_MINUTE

    /** Whole minutes played (rounded down). */
    fun minutesPlayed(playedMillis: Long): Int = (playedMillis / MILLIS_PER_MINUTE).toInt()
}

/** Measures how long a game has been on screen. Pure Kotlin, with a clock that tests can replace. */
class PlayStopwatch(
    private val nowMillis: () -> Long = { System.nanoTime() / 1_000_000 }
) {
    private var startedAt: Long? = null

    val isRunning: Boolean get() = startedAt != null

    /** Starts timing. Does nothing if already running, so repeated calls never reset the time. */
    fun start() {
        if (startedAt == null) startedAt = nowMillis()
    }

    /** Stops and returns the milliseconds since [start] (0 if it was not running). */
    fun stop(): Long {
        val started = startedAt ?: return 0L
        startedAt = null
        return (nowMillis() - started).coerceAtLeast(0L)
    }
}
