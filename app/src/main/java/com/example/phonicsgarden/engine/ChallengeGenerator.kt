package com.example.phonicsgarden.engine

import com.example.phonicsgarden.data.local.entity.Grapheme
import javax.inject.Inject
import kotlin.random.Random

/** Builds Challenges from graphemes. Pure Kotlin logic, so it is easy to unit test. */
class ChallengeGenerator @Inject constructor() {

    fun generate(
        target: Grapheme,
        pool: List<Grapheme>,
        optionCount: Int = 4,
        random: Random = Random.Default
    ): Challenge {
        val distractors = pool
            .filter { it.letter != target.letter }
            .distinctBy { it.letter }
            .shuffled(random)
            .take(optionCount - 1)
            .map { it.letter }

        val options = (distractors + target.letter).shuffled(random)

        return Challenge(
            prompt = target.spokenSound,
            correctAnswer = target.letter,
            options = options,
            graphemeId = target.id
        )
    }
}
