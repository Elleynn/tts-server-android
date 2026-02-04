package com.elleynn.tts.synthesizer

data class SystemParams(
    val text: String = "",
    val speed: Float = 1f,
    val volume: Float = 1f,
    val pitch: Float = 1f
)