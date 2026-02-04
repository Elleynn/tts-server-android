package com.elleynn.tts.synthesizer

import com.elleynn.database.entities.systts.BgmConfiguration

interface ITtsRepository {
    fun init()
    fun destroy()

    fun getTts(id: Long): TtsConfiguration?
    fun getAllTts(): Map<Long, TtsConfiguration>
    fun getAllBgm(): List<BgmConfiguration>
}