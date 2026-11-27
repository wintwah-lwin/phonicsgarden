package com.example.phonicsgarden.testutil

import com.example.phonicsgarden.engine.TracePoint
import kotlin.math.hypot

/** Simulates a smooth finger path by adding points between consecutive waypoints. */
fun densify(points: List<TracePoint>, step: Float = 0.03f): List<TracePoint> {
    val result = mutableListOf<TracePoint>()
    points.zipWithNext().forEach { (a, b) ->
        val steps = (hypot(b.x - a.x, b.y - a.y) / step).toInt().coerceAtLeast(1)
        for (i in 0 until steps) {
            val t = i.toFloat() / steps
            result += TracePoint(a.x + (b.x - a.x) * t, a.y + (b.y - a.y) * t)
        }
    }
    result += points.last()
    return result
}

/** A scribble far away from any letter. */
val scribble = listOf(TracePoint(0.97f, 0.97f), TracePoint(0.99f, 0.99f))
