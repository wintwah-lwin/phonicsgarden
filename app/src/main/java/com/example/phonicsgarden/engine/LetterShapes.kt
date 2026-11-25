package com.example.phonicsgarden.engine

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Letter formation guides for lowercase letters, as ordered strokes of waypoints.
 *
 * Coordinates are normalised to a square (0.0 to 1.0, y grows downward):
 * ascender line ~0.10, x-height line ~0.35, baseline ~0.75, descender ~0.95.
 * Stroke order and direction follow common school letter formation, so children
 * practise the correct way to write each letter (not just its shape).
 */
object LetterShapes {

    private fun pts(vararg xy: Float): List<TracePoint> =
        xy.toList().chunked(2).map { TracePoint(it[0], it[1]) }

    /** Points on an ellipse. 0 degrees is right; decreasing angles go anticlockwise on screen. */
    private fun arc(
        cx: Double, cy: Double, rx: Double, ry: Double,
        startDeg: Double, endDeg: Double, steps: Int
    ): List<TracePoint> = (0..steps).map { i ->
        val deg = startDeg + (endDeg - startDeg) * i / steps
        val rad = deg * PI / 180.0
        TracePoint((cx + rx * cos(rad)).toFloat(), (cy + ry * sin(rad)).toFloat())
    }

    // Shared pieces
    private fun roundBody() = arc(0.5, 0.55, 0.22, 0.20, -45.0, -405.0, 9)
    private fun bowl() = pts(0.31f, 0.45f, 0.42f, 0.38f, 0.56f, 0.38f, 0.68f, 0.46f, 0.72f, 0.58f, 0.66f, 0.70f, 0.52f, 0.75f, 0.34f, 0.72f)
    private fun arch() = pts(0.30f, 0.52f, 0.40f, 0.41f, 0.54f, 0.37f, 0.67f, 0.43f, 0.70f, 0.55f, 0.70f, 0.75f)

    private val shapes: Map<String, List<List<TracePoint>>> = mapOf(
        "o" to listOf(roundBody()),
        "c" to listOf(arc(0.5, 0.55, 0.22, 0.20, -45.0, -315.0, 7)),
        "a" to listOf(roundBody(), pts(0.72f, 0.35f, 0.72f, 0.55f, 0.72f, 0.75f)),
        "d" to listOf(roundBody(), pts(0.72f, 0.10f, 0.72f, 0.40f, 0.72f, 0.75f)),
        "g" to listOf(
            roundBody(),
            pts(0.72f, 0.35f, 0.72f, 0.60f, 0.72f, 0.80f, 0.66f, 0.91f, 0.52f, 0.95f, 0.38f, 0.91f)
        ),
        "e" to listOf(
            pts(0.28f, 0.55f, 0.50f, 0.55f, 0.72f, 0.55f) + arc(0.5, 0.55, 0.22, 0.20, -60.0, -300.0, 7)
        ),
        "s" to listOf(
            pts(0.68f, 0.42f, 0.55f, 0.36f, 0.40f, 0.40f, 0.35f, 0.50f, 0.50f, 0.57f, 0.65f, 0.63f, 0.65f, 0.70f, 0.50f, 0.75f, 0.34f, 0.70f)
        ),
        "b" to listOf(pts(0.30f, 0.10f, 0.30f, 0.42f, 0.30f, 0.75f), bowl()),
        "p" to listOf(pts(0.30f, 0.35f, 0.30f, 0.65f, 0.30f, 0.95f), bowl()),
        "h" to listOf(pts(0.30f, 0.10f, 0.30f, 0.42f, 0.30f, 0.75f), arch()),
        "n" to listOf(pts(0.30f, 0.35f, 0.30f, 0.55f, 0.30f, 0.75f), arch()),
        "m" to listOf(
            pts(0.20f, 0.35f, 0.20f, 0.55f, 0.20f, 0.75f),
            pts(0.20f, 0.50f, 0.28f, 0.40f, 0.38f, 0.37f, 0.46f, 0.44f, 0.48f, 0.55f, 0.48f, 0.75f),
            pts(0.48f, 0.52f, 0.56f, 0.41f, 0.66f, 0.37f, 0.74f, 0.43f, 0.77f, 0.55f, 0.77f, 0.75f)
        ),
        "r" to listOf(
            pts(0.35f, 0.35f, 0.35f, 0.55f, 0.35f, 0.75f),
            pts(0.35f, 0.52f, 0.44f, 0.41f, 0.56f, 0.37f, 0.68f, 0.39f)
        ),
        "u" to listOf(
            pts(0.30f, 0.35f, 0.30f, 0.55f, 0.36f, 0.70f, 0.50f, 0.76f, 0.64f, 0.70f),
            pts(0.70f, 0.35f, 0.70f, 0.55f, 0.70f, 0.75f)
        ),
        "t" to listOf(
            pts(0.50f, 0.15f, 0.50f, 0.40f, 0.50f, 0.65f, 0.56f, 0.73f, 0.66f, 0.75f),
            pts(0.32f, 0.38f, 0.50f, 0.38f, 0.68f, 0.38f)
        ),
        "i" to listOf(
            pts(0.50f, 0.35f, 0.50f, 0.55f, 0.50f, 0.75f),
            pts(0.50f, 0.18f) // the dot: a single tap
        ),
        "k" to listOf(
            pts(0.30f, 0.10f, 0.30f, 0.42f, 0.30f, 0.75f),
            pts(0.70f, 0.35f, 0.50f, 0.47f, 0.30f, 0.58f),
            pts(0.45f, 0.52f, 0.58f, 0.64f, 0.72f, 0.75f)
        ),
        "f" to listOf(
            pts(0.68f, 0.18f, 0.58f, 0.11f, 0.46f, 0.15f, 0.42f, 0.28f, 0.42f, 0.50f, 0.42f, 0.75f),
            pts(0.28f, 0.38f, 0.42f, 0.38f, 0.62f, 0.38f)
        )
    )

    fun hasShape(letter: String): Boolean = letter in shapes

    /** Strokes in writing order, or null if the letter has no guide yet. */
    fun strokes(letter: String): List<List<TracePoint>>? = shapes[letter]

    /** All waypoints in writing order, ready for [TraceValidator.isValid]. */
    fun waypoints(letter: String): List<TracePoint> = shapes[letter]?.flatten() ?: emptyList()
}
