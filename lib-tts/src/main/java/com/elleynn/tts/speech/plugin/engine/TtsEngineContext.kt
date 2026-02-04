package com.elleynn.tts.speech.plugin.engine

import android.content.Context
import androidx.annotation.Keep
import com.elleynn.database.entities.systts.source.PluginTtsSource
import com.elleynn.script.simple.ext.JsExtensions

/**
 * @param tts 在JS中用 `ttsrv.tts` 访问
 */
@Keep
data class TtsEngineContext(
    var tts: PluginTtsSource,
    val userVars: Map<String, String> = mutableMapOf(),
    override val context: Context,
    override val engineId: String
) : JsExtensions(context, engineId)