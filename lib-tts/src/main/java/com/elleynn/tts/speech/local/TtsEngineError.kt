package com.elleynn.tts.speech.local

sealed interface TtsEngineError {
    object Initialization : TtsEngineError
    object Engine : TtsEngineError
    object File : TtsEngineError
}