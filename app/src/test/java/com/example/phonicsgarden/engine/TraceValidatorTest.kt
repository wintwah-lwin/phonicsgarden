package com.example.phonicsgarden.engine

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TraceValidatorTest {

    private val waypoints = listOf(
        TracePoint(0.5f, 0.1f),
        TracePoint(0.5f, 0.5f),
        TracePoint(0.5f, 0.9f)
    )

    @Test
    fun `path through all waypoints in order is valid`() {
        val path = listOf(
            TracePoint(0.5f, 0.1f), TracePoint(0.52f, 0.3f),
            TracePoint(0.5f, 0.5f), TracePoint(0.48f, 0.7f), TracePoint(0.5f, 0.9f)
        )
        assertTrue(TraceValidator.isValid(path, waypoints))
    }

    @Test
    fun `path in the wrong order is invalid`() {
        val reversed = waypoints.reversed()
        assertFalse(TraceValidator.isValid(reversed, waypoints))
    }

    @Test
    fun `path that misses a waypoint is invalid`() {
        val path = listOf(TracePoint(0.5f, 0.1f), TracePoint(0.5f, 0.9f))
        assertFalse(TraceValidator.isValid(path, waypoints))
    }

    @Test
    fun `empty path or empty waypoints are invalid`() {
        assertFalse(TraceValidator.isValid(emptyList(), waypoints))
        assertFalse(TraceValidator.isValid(waypoints, emptyList()))
    }

    @Test
    fun `larger tolerance accepts a sloppier trace`() {
        val sloppy = waypoints.map { TracePoint(it.x + 0.2f, it.y) }
        assertFalse(TraceValidator.isValid(sloppy, waypoints, tolerance = 0.1f))
        assertTrue(TraceValidator.isValid(sloppy, waypoints, tolerance = 0.25f))
    }
}
