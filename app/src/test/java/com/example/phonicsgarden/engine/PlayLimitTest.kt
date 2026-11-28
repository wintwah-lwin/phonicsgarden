package com.example.phonicsgarden.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PlayLimitTest {

    private val minute = 60_000L

    @Test
    fun `no limit never stops play`() {
        assertFalse(PlayLimit.isReached(playedMillis = 10 * 60 * minute, limitMinutes = 0))
    }

    @Test
    fun `below the limit play continues`() {
        assertFalse(PlayLimit.isReached(playedMillis = 9 * minute + 59_999, limitMinutes = 10))
    }

    @Test
    fun `reaching the limit exactly counts as reached`() {
        assertTrue(PlayLimit.isReached(playedMillis = 10 * minute, limitMinutes = 10))
    }

    @Test
    fun `beyond the limit is reached`() {
        assertTrue(PlayLimit.isReached(playedMillis = 25 * minute, limitMinutes = 15))
    }

    @Test
    fun `negative limits are treated as off`() {
        assertFalse(PlayLimit.isReached(playedMillis = 100 * minute, limitMinutes = -5))
    }

    @Test
    fun `minutes played rounds down`() {
        assertEquals(0, PlayLimit.minutesPlayed(59_999))
        assertEquals(1, PlayLimit.minutesPlayed(60_000))
        assertEquals(7, PlayLimit.minutesPlayed(7 * minute + 30_000))
    }

    @Test
    fun `options start with off and only offer gentle amounts`() {
        assertEquals(0, PlayLimit.OPTIONS_MINUTES.first())
        assertTrue(PlayLimit.OPTIONS_MINUTES.all { it in 0..30 })
        assertEquals(PlayLimit.OPTIONS_MINUTES.sorted(), PlayLimit.OPTIONS_MINUTES)
    }
}
