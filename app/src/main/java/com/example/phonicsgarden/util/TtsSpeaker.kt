package com.example.phonicsgarden.util

import android.content.Context
import android.speech.tts.TextToSpeech
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/** Thin wrapper around Android TextToSpeech using an Australian English voice. */
@Singleton
class TtsSpeaker @Inject constructor(
    @ApplicationContext context: Context
) : TextToSpeech.OnInitListener {

    private var ready = false
    private val tts = TextToSpeech(context, this)

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.forLanguageTag("en-AU")
            tts.setSpeechRate(0.8f) // slower for young learners
            ready = true
        }
    }

    fun speak(text: String) {
        if (ready) tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, text)
    }
}
