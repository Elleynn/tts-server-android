package com.elleynn.tts.error

import com.elleynn.tts.ConfigType


sealed interface TextProcessorError {
    data class MissingConfig(val type: ConfigType, val details: String = "") :
        TextProcessorError

    data class HandleText(val error: Throwable) : TextProcessorError
    data class MissingRule(val id: String) : TextProcessorError
    object Initialization : TextProcessorError
}