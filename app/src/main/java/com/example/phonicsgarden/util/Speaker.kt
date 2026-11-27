package com.example.phonicsgarden.util

/** Anything that can speak text aloud. Lets ViewModels be unit tested without Android TTS. */
interface Speaker {
    fun speak(text: String)
}
