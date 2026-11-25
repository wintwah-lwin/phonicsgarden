package com.example.phonicsgarden.engine

/** One question shown by a minigame. All games render Challenges; none build them directly. */
data class Challenge(
    /** The text-to-speech prompt (e.g. "sss"). */
    val prompt: String,
    val correctAnswer: String,
    /** Correct answer plus distractors, already shuffled. */
    val options: List<String>,
    val graphemeId: Long
) {
    fun isCorrect(answer: String): Boolean = answer == correctAnswer
}
