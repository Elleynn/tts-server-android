package com.elleynn.database.entities.systts.v1.tts

import androidx.annotation.Keep
import com.elleynn.database.entities.plugin.Plugin
import com.elleynn.database.entities.systts.AudioParams
import com.elleynn.database.entities.systts.BasicAudioFormat
import com.elleynn.database.entities.systts.SpeechRuleInfo
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Transient

@Keep
@Parcelize
@kotlinx.serialization.Serializable
@SerialName("plugin")
data class PluginTTS(
    val pluginId: String = "",
    override var locale: String = "",
    var voice: String = "",
    // 插件附加数据
    var data: MutableMap<String, String> = mutableMapOf(),

    override var pitch: Int = 50,
    override var volume: Int = 50,
    override var rate: Int = 50,

    override var audioFormat: com.elleynn.database.entities.systts.BasicAudioFormat = BasicAudioFormat(),
    override var audioPlayer: PlayerParams = PlayerParams(),
    override var audioParams: AudioParams = AudioParams(),
    @Transient
    override var speechRule: SpeechRuleInfo = SpeechRuleInfo(),
    @Transient
    var plugin: Plugin? = null,
) : ITextToSpeechEngine() {
    override fun getType(): String = ""

}