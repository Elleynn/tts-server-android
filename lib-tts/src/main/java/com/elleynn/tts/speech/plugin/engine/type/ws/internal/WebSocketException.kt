package com.elleynn.tts.speech.plugin.engine.type.ws.internal

import okhttp3.Response

data class WebSocketException(val response: Response? = null) : Exception()