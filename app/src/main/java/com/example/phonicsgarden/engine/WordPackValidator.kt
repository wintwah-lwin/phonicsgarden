package com.example.phonicsgarden.engine

/** One raw word from a downloaded content pack, before it has been checked. */
data class PackEntry(val text: String, val emoji: String)

/** A word that passed every check and is safe to show to a child. */
data class AcceptedWord(val text: String, val emoji: String, val firstLetter: String, val phase: Int)

data class PackValidation(val accepted: List<AcceptedWord>, val rejected: Int)

/**
 * Checks downloaded content before it is stored. Anything that does not look like a simple
 * "word + picture" pair for a taught letter is rejected, so a bad or tampered pack cannot put
 * unexpected text in front of a child.
 */
object WordPackValidator {
    const val MAX_WORDS = 200
    const val MIN_WORD_LENGTH = 2
    const val MAX_WORD_LENGTH = 12
    const val MAX_EMOJI_LENGTH = 16

    private val lowercaseLetters = Regex("^[a-z]+$")

    /** @param taughtLetters taught letter -> phase it is introduced in. */
    fun validate(entries: List<PackEntry>, taughtLetters: Map<String, Int>): PackValidation {
        val seen = mutableSetOf<String>()
        val accepted = mutableListOf<AcceptedWord>()
        var rejected = 0

        for (entry in entries) {
            val text = entry.text.trim().lowercase()
            val emoji = entry.emoji.trim()
            val firstLetter = text.take(1)
            val phase = taughtLetters[firstLetter]

            val acceptable = phase != null &&
                isSimpleWord(text) &&
                isPictureOnly(emoji) &&
                accepted.size < MAX_WORDS &&
                seen.add(text)

            if (acceptable && phase != null) {
                accepted += AcceptedWord(text, emoji, firstLetter, phase)
            } else {
                rejected++
            }
        }
        return PackValidation(accepted, rejected)
    }

    private fun isSimpleWord(text: String): Boolean =
        text.length in MIN_WORD_LENGTH..MAX_WORD_LENGTH && lowercaseLetters.matches(text)

    /** A picture must be short and contain no letters, digits or spaces (so it cannot carry text). */
    private fun isPictureOnly(emoji: String): Boolean =
        emoji.isNotEmpty() &&
            emoji.length <= MAX_EMOJI_LENGTH &&
            emoji.none { it.isLetterOrDigit() || it.isWhitespace() }
}
