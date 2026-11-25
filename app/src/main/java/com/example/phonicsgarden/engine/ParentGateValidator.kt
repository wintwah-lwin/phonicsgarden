package com.example.phonicsgarden.engine

import kotlin.random.Random

/**
 * Generates simple math questions for adult verification (Parent Gate)
 * to ensure young children cannot change settings or view statistics.
 */
data class MathQuestion(
    val num1: Int,
    val num2: Int,
    val answer: Int
) {
    val prompt: String get() = "What is $num1 + $num2?"
}

object ParentGateValidator {
    fun generateQuestion(random: Random = Random): MathQuestion {
        val a = random.nextInt(3, 10)
        val b = random.nextInt(4, 10)
        return MathQuestion(a, b, a + b)
    }

    fun validateAnswer(input: String, expectedAnswer: Int): Boolean {
        val parsed = input.trim().toIntOrNull()
        return parsed != null && parsed == expectedAnswer
    }
}
