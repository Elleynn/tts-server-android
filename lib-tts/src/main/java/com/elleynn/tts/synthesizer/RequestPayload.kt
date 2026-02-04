package com.elleynn.tts.synthesizer

data class RequestPayload(val params: SystemParams, val config: TtsConfiguration) {
    val text: String
        get() = params.text
}