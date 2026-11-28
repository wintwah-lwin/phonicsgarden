package com.example.phonicsgarden.util

/** The short sounds the games can play. Kept free of Android types so it can be unit tested. */
enum class SoundEffect {
    /** A bubble popping (Bubble Pop, correct answer). */
    POP,

    /** The monster eating (Feed the Monster, correct answer). */
    CHOMP,

    /** A cheerful chime. */
    CHIME,

    /** A soft, friendly "try again" boop. Deliberately quiet, never a buzzer. */
    SOFT,

    /** A flower blooming (Tracing Garden, letter traced). */
    BLOOM,

    /** A short fanfare when a round is finished. */
    FANFARE
}

/** Plays [SoundEffect]s. ViewModels only call this when the parent's Sound setting is on. */
interface SoundEffects {
    fun play(effect: SoundEffect)
}
