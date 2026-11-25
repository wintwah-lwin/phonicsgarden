package com.example.phonicsgarden.engine

import com.example.phonicsgarden.data.local.entity.Grapheme
import com.example.phonicsgarden.data.local.entity.Word
import javax.inject.Inject
import kotlin.random.Random

/** "Feed me something that starts with /s/": pick the picture whose word starts with that sound. */
data class FeedChallenge(
    val prompt: String,
    /** All words that start with the target sound (c and k share a sound, for example). */
    val correctWords: Set<String>,
    /** One correct word plus distractors, shuffled. */
    val options: List<String>,
    val graphemeId: Long
) {
    fun isCorrect(word: String): Boolean = word in correctWords
}

class WordChallengeGenerator @Inject constructor() {

    /**
     * Returns null if there is no word for this sound, or no distractor to compare against.
     * Distractors never start with the target sound, so there is only ever one right answer type.
     */
    fun generate(
        target: Grapheme,
        graphemes: List<Grapheme>,
        words: List<Word>,
        optionCount: Int = 3,
        random: Random = Random.Default
    ): FeedChallenge? {
        val soundByLetter = graphemes.associate { it.letter to it.spokenSound }
        val (matching, others) = words.partition { soundByLetter[it.firstLetter] == target.spokenSound }
        if (matching.isEmpty()) return null

        val distractors = others
            .filter { it.firstLetter in soundByLetter }
            .distinctBy { it.firstLetter }
            .shuffled(random)
            .take(optionCount - 1)
        if (distractors.isEmpty()) return null

        val correct = matching.random(random)
        val options = (distractors + correct).map { it.text }.shuffled(random)

        return FeedChallenge(
            prompt = "Feed me something that starts with ${target.spokenSound}",
            correctWords = matching.map { it.text }.toSet(),
            options = options,
            graphemeId = target.id
        )
    }
}
