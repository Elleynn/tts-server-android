package com.elleynn.tts.speech

interface ILifeState {
    suspend fun onInit()
    fun onStop()
    fun onDestroy()
}