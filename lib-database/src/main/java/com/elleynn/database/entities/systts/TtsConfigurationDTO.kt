package com.elleynn.database.entities.systts

import com.elleynn.database.entities.systts.source.TextToSpeechSource
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
@SerialName("tts")
data class TtsConfigurationDTO(
    val speechRule: SpeechRuleInfo = SpeechRuleInfo(),
    val audioParams: AudioParams = AudioParams(),
    val audioFormat: BasicAudioFormat = BasicAudioFormat(),
    val source: TextToSpeechSource,
) : IConfiguration() {
    fun isNeedDecode(): Boolean{
        return source.shouldDecode(audioFormat)
    }
}