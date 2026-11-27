package com.example.phonicsgarden.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/** A letter-sound pair taught in a phonics phase (e.g. "s" -> "sss", phase 1). */
@Entity(tableName = "graphemes")
data class Grapheme(
    @PrimaryKey val id: Long,
    val letter: String,
    /** What the text-to-speech engine says for this sound. */
    val spokenSound: String,
    val phase: Int
)

/**
 * A simple word with an emoji picture, used by Feed the Monster.
 * Every word must have a picture (enforced by the NOT NULL column), so nothing without an
 * approved picture can ever reach a child.
 */
@Entity(tableName = "words", indices = [Index(value = ["text"], unique = true)])
data class Word(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val firstLetter: String,
    val phase: Int = 1,
    val emoji: String,
    /** Where the word came from: [SOURCE_BUNDLED] with the app or [SOURCE_PACK] downloaded. */
    val source: String = SOURCE_BUNDLED
) {
    companion object {
        const val SOURCE_BUNDLED = "bundled"
        const val SOURCE_PACK = "pack"
    }
}

/** One answer given by the child. Stored only on the device. */
@Entity(tableName = "attempts", indices = [Index(value = ["timestamp"])])
data class Attempt(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val graphemeId: Long,
    val game: String,
    val correct: Boolean,
    val timestamp: Long
)

/** Leitner-style mastery box for each sound (0 = new, higher = better known). */
@Entity(tableName = "mastery")
data class Mastery(
    @PrimaryKey val graphemeId: Long,
    val box: Int,
    val nextReviewAt: Long
)
