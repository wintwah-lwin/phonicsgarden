package com.example.phonicsgarden.engine

import kotlin.math.hypot

/** A point in normalised letter space (0.0 to 1.0 on both axes). */
data class TracePoint(val x: Float, val y: Float)

/** Checks that a finger path passes near each waypoint of a letter, in order. */
object TraceValidator {

    fun isValid(
        path: List<TracePoint>,
        waypoints: List<TracePoint>,
        tolerance: Float = 0.12f
    ): Boolean {
        if (path.isEmpty() || waypoints.isEmpty()) return false

        var searchFrom = 0
        for (waypoint in waypoints) {
            val hit = (searchFrom until path.size).firstOrNull { index ->
                distance(path[index], waypoint) <= tolerance
            } ?: return false
            searchFrom = hit
        }
        return true
    }

    private fun distance(a: TracePoint, b: TracePoint): Float = hypot(a.x - b.x, a.y - b.y)
}
