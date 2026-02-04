package com.elleynn.tts.synthesizer.event

import com.elleynn.tts.synthesizer.BgmSource
import com.elleynn.tts.synthesizer.RequestPayload

sealed interface NormalEvent : Event {
    data class Request(
        val request: RequestPayload,
        val retries: Int,
    ) : NormalEvent

    data class ReadAllFromStream(
        val request: RequestPayload,
        val size: Int,
        val costTime: Long,
    ) : NormalEvent

    data class HandleStream(
        val request: RequestPayload,
    ) : NormalEvent

    data class DirectPlay(val request: RequestPayload) : NormalEvent
    data class StandbyTts(val request: RequestPayload) : NormalEvent
    data object RequestCountEnded : NormalEvent

    data class BgmCurrentPlaying(val source: BgmSource) : NormalEvent
}