package com.elleynn.tts.synthesizer

import java.nio.ByteBuffer

fun interface PcmAudioDataListener {
    fun receive(data: ByteBuffer)
}