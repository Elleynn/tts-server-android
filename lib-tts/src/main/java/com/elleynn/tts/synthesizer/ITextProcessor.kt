package com.elleynn.tts.synthesizer

import android.content.Context
import com.github.michaelbull.result.Result

interface ITextProcessor {
    fun init(
        context: Context,
        configs: Map<Long, TtsConfiguration>,
    ): Result<Unit, com.elleynn.tts.error.TextProcessorError>

    fun process(
        text: String,
        forceConfig: TtsConfiguration? = null,
    ): Result<List<TextSegment>, com.elleynn.tts.error.TextProcessorError>
}