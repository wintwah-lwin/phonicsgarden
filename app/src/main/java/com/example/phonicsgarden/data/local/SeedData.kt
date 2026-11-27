package com.example.phonicsgarden.data.local

import com.example.phonicsgarden.data.local.entity.Grapheme
import com.example.phonicsgarden.data.local.entity.Word

/**
 * Bundled starter content so the app works fully offline.
 * NOTE: spokenSound values are approximations for text-to-speech. Recorded audio clips
 * of real phoneme pronunciations would be more accurate (future improvement).
 */
object SeedData {
    val graphemes = listOf(
        // Phase 1: s a t p i n
        Grapheme(1, "s", "sss", 1),
        Grapheme(2, "a", "ah", 1),
        Grapheme(3, "t", "tuh", 1),
        Grapheme(4, "p", "puh", 1),
        Grapheme(5, "i", "ih", 1),
        Grapheme(6, "n", "nnn", 1),
        // Phase 2: m d g o c k
        Grapheme(7, "m", "mmm", 2),
        Grapheme(8, "d", "duh", 2),
        Grapheme(9, "g", "guh", 2),
        Grapheme(10, "o", "aw", 2),
        Grapheme(11, "c", "kuh", 2),
        Grapheme(12, "k", "kuh", 2),
        // Phase 3: e u r h b f
        Grapheme(13, "e", "eh", 3),
        Grapheme(14, "u", "uh", 3),
        Grapheme(15, "r", "rrr", 3),
        Grapheme(16, "h", "huh", 3),
        Grapheme(17, "b", "buh", 3),
        Grapheme(18, "f", "fff", 3)
    )

    /** Every word with a picture, tagged with the phase of its first letter. */
    val words: List<Word> = WordPictures.emoji.map { (text, emoji) ->
        val letter = text.first().toString()
        Word(
            text = text,
            firstLetter = letter,
            phase = graphemes.first { it.letter == letter }.phase,
            emoji = emoji
        )
    }
}
