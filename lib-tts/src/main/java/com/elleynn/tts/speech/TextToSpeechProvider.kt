package com.elleynn.tts.speech

import com.elleynn.database.entities.systts.source.TextToSpeechSource
import com.elleynn.tts.synthesizer.SystemParams
import java.io.InputStream

abstract class TextToSpeechProvider<in T : TextToSpeechSource> : ILifeState {
    abstract var state: EngineState
    open fun isSyncPlay(source: T): Boolean {
        return false
    }

    override fun onStop() {
    }


    abstract suspend fun getStream(params: SystemParams, source: T): InputStream

    open suspend fun syncPlay(params: SystemParams, source: T) {
        TODO("not yet implemented")
    }
}