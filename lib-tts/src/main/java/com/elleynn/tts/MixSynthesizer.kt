package com.elleynn.tts

import com.elleynn.tts.synthesizer.AbstractMixSynthesizer
import com.elleynn.tts.synthesizer.IBgmPlayer
import com.elleynn.tts.synthesizer.IResultProcessor
import com.elleynn.tts.synthesizer.ITextProcessor
import com.elleynn.tts.synthesizer.ITtsRepository
import com.elleynn.tts.synthesizer.ITtsRequester
import io.github.oshai.kotlinlogging.KotlinLogging
import splitties.init.appCtx

open class MixSynthesizer(
    final override val context: SynthesizerContext
) : AbstractMixSynthesizer() {
    override var textProcessor: ITextProcessor = TextProcessor(context)
    override var ttsRequester: ITtsRequester = DefaultTtsRequester(context)
    override var streamProcessor: IResultProcessor = DefaultResultProcessor(context)
    override var repo: ITtsRepository = TtsRepository(context)
    override var bgmPlayer: IBgmPlayer = BgmPlayer(context)

    companion object {
        val global by lazy {
            val logger = KotlinLogging.logger("TtsManager")
            MixSynthesizer(
                SynthesizerContext(
                    androidContext = appCtx,
                    logger = logger,
                    cfg = SynthesizerConfig()
                )
            )
        }
    }
}