package com.example.phonicsgarden.engine

import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

/**
 * Where one floating bubble lives. All values are fractions so the layout works on any screen.
 * @param lane horizontal centre of the bubble's lane (0 = left edge, 1 = right edge)
 * @param phase how far through its upward journey the bubble starts (0..1)
 * @param swayPhase where in its gentle side-to-side wobble the bubble starts (0..1)
 */
data class BubbleSpec(val lane: Float, val phase: Float, val swayPhase: Float)

/**
 * Pure maths for the Bubble Pop field: lay bubbles out, then ask where each one is for a given
 * animation `progress` (0..1, repeating). Keeping this free of Android makes it unit testable.
 */
object BubbleField {
    /** Side-to-side wobble, as a fraction of the field width. Small, so bubbles stay in their lane. */
    const val SWAY_AMPLITUDE = 0.03f

    /** Whole number of wobbles per loop, so the animation repeats without a jump. */
    const val SWAY_CYCLES = 4

    /** Stops the first bubble starting right at the bottom edge where it cannot be seen yet. */
    private const val PHASE_OFFSET = 0.1f

    /**
     * Lays out [count] bubbles in evenly spaced lanes with evenly staggered heights, so two
     * bubbles are never level with each other and never crowd one spot.
     */
    fun layout(count: Int, random: Random = Random.Default): List<BubbleSpec> {
        if (count <= 0) return emptyList()
        val lanes = List(count) { (it + 0.5f) / count }.shuffled(random)
        return List(count) { i ->
            BubbleSpec(
                lane = lanes[i],
                phase = (PHASE_OFFSET + i.toFloat() / count) % 1f,
                swayPhase = random.nextFloat()
            )
        }
    }

    /** 0 = just below the field, 1 = just above it. Bubbles only ever travel upwards. */
    fun verticalFraction(spec: BubbleSpec, progress: Float): Float = (spec.phase + progress) % 1f

    /** Horizontal centre as a fraction of the field width, including the gentle wobble. */
    fun horizontalCenter(spec: BubbleSpec, progress: Float): Float {
        val angle = 2.0 * PI * (SWAY_CYCLES * progress + spec.swayPhase)
        return spec.lane + (SWAY_AMPLITUDE * sin(angle)).toFloat()
    }
}
