package com.elleynn.tts.synthesizer.event

fun interface IEventDispatcher {
    fun dispatch(event: Event)
}