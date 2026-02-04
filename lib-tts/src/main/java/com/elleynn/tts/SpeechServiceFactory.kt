package com.elleynn.tts

import android.content.Context
import com.elleynn.database.dbm
import com.elleynn.database.entities.systts.source.TextToSpeechSource
import com.elleynn.database.entities.systts.source.LocalTtsSource
import com.elleynn.database.entities.systts.source.PluginTtsSource
import com.elleynn.tts.speech.TextToSpeechProvider
import com.elleynn.tts.speech.local.LocalTtsProvider
import com.elleynn.tts.speech.plugin.PluginTtsProvider

@Suppress("UNCHECKED_CAST")
object SpeechServiceFactory {
    fun createEngine(context: Context, source: TextToSpeechSource): TextToSpeechProvider<TextToSpeechSource>? {
        return when (source) {
            is LocalTtsSource -> LocalTtsProvider(context, source.engine)
            is PluginTtsSource -> {
                PluginTtsProvider(
                    context,
                    source.plugin ?: dbm.pluginDao.getEnabled(source.pluginId) ?: return null
                )
            }

            else -> null
        } as TextToSpeechProvider<TextToSpeechSource>?
    }
}