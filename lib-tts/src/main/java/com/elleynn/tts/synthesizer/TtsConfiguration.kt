package com.elleynn.tts.synthesizer

import com.elleynn.database.entities.systts.AudioParams
import com.elleynn.database.entities.systts.BasicAudioFormat
import com.elleynn.database.entities.systts.SpeechRuleInfo
import com.elleynn.database.entities.systts.TtsConfigurationDTO
import com.elleynn.database.entities.systts.source.TextToSpeechSource

data class TtsConfiguration(
    val speechInfo: SpeechRuleInfo = SpeechRuleInfo(),
    val audioParams: AudioParams = AudioParams(),
    val audioFormat: BasicAudioFormat = BasicAudioFormat(),
    val source: TextToSpeechSource,
    val tag: Any? = null,

    val standbyConfig: TtsConfiguration? = null,
) {
    fun shouldDecode(): Boolean {
        return source.shouldDecode(audioFormat)
    }

    companion object {
        fun TtsConfigurationDTO.toVO(): TtsConfiguration {
            return TtsConfiguration(
                speechInfo = speechRule,
                audioParams = audioParams,
                audioFormat = audioFormat,
                source = source,
                standbyConfig = null,
            )
        }

        fun TtsConfiguration.toDTO(): TtsConfigurationDTO {
            return TtsConfigurationDTO(
                speechRule = speechInfo,
                audioParams = audioParams,
                audioFormat = audioFormat,
                source = source,
            )
        }
    }
}