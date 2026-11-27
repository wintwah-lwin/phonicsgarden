package com.example.phonicsgarden.util

import android.content.Context
import android.speech.tts.TextToSpeech
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/** [Speaker] backed by Android TextToSpeech, using an Australian English voice. */
@Singleton
class TtsSpeaker @Inject constructor(
    @ApplicationContext context: Context
) : Speaker, TextToSpeech.OnInitListener {

    @Volatile private var ready = false

    /** Text requested before the engine finished starting up (e.g. the first prompt of a game). */
    @Volatile private var pending: String? = null

    private val tts = TextToSpeech(context, this)

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.forLanguageTag("en-AU")
            tts.setSpeechRate(0.8f) // slower for young learners
            ready = true
            pending?.let { speak(it) }
            pending = null
        }
    }

    override fun speak(text: String) {
        if (ready) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, text)
        } else {
            pending = text // play as soon as the engine is ready instead of dropping it
        }
    }
}
