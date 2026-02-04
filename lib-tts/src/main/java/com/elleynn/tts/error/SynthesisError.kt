package com.elleynn.tts.error

sealed interface SynthesisError {
    data object ConfigEmpty : SynthesisError
    data class TextHandle(val err: TextProcessorError) : SynthesisError
    data class PresetMissing(val id: Long) : SynthesisError
}