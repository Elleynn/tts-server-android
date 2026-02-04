package com.elleynn.tts.speech

import com.elleynn.database.entities.systts.AudioParams
import com.elleynn.database.entities.systts.source.TextToSpeechSource

interface DecoderInfo<T : TextToSpeechSource> {
    fun apply(source: T): AudioParams
}