package com.elleynn.tts.error

sealed interface RequesterError {
    data class RequestError(val error: Throwable) : RequesterError
    data class StateError(val message: String) : RequesterError
}