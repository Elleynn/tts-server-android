package com.elleynn.tts.speech.local

import android.os.Bundle

data class SpeechParameters(
    val voice: String,
    val locale: String,
    val speed: Float,
    val volume: Float,
    val pitch: String,
    val bundle: Bundle = Bundle()
) {
}