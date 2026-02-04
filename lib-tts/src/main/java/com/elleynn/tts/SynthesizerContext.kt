package com.elleynn.tts

import android.content.Context
import com.elleynn.tts.synthesizer.event.IEventDispatcher
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

data class SynthesizerContext(
    var androidContext: Context,
    var logger: KLogger = KotlinLogging.logger { "tts-default" },
    var cfg: SynthesizerConfig = SynthesizerConfig(),
    var event: IEventDispatcher? = null
) {
}