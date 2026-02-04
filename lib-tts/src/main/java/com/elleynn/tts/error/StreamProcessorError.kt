package com.elleynn.tts.error

sealed interface StreamProcessorError {
    data class HandleError(val error: Throwable) : StreamProcessorError
    data class AudioSource(val error: Throwable) : StreamProcessorError
    data class AudioDecoding(val error: Throwable) : StreamProcessorError
}