package com.example.phonicsgarden.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PlayStopwatchTest {

    private var now = 1_000L
    private val stopwatch = PlayStopwatch { now }

    @Test
    fun `measures the time between start and stop`() {
        stopwatch.start()
        now += 4_500

        assertEquals(4_500L, stopwatch.stop())
    }

    @Test
    fun `stopping without starting gives zero`() {
        assertEquals(0L, stopwatch.stop())
    }

    @Test
    fun `starting twice does not restart the clock`() {
        stopwatch.start()
        now += 1_000
        stopwatch.start()
        now += 1_000

        assertEquals(2_000L, stopwatch.stop())
    }

    @Test
    fun `a second stop adds nothing`() {
        stopwatch.start()
        now += 3_000
        stopwatch.stop()

        assertEquals(0L, stopwatch.stop())
    }

    @Test
    fun `can be used again after stopping`() {
        stopwatch.start()
        now += 1_000
        stopwatch.stop()

        stopwatch.start()
        now += 2_000

        assertEquals(2_000L, stopwatch.stop())
    }

    @Test
    fun `reports whether it is running`() {
        assertFalse(stopwatch.isRunning)
        stopwatch.start()
        assertTrue(stopwatch.isRunning)
        stopwatch.stop()
        assertFalse(stopwatch.isRunning)
    }

    @Test
    fun `a clock that goes backwards never gives negative time`() {
        stopwatch.start()
        now -= 500

        assertEquals(0L, stopwatch.stop())
    }
}
