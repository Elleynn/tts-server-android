package com.elleynn.tts_server_android.compose.systts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.drake.net.utils.withMain
import com.elleynn.common.audio.AudioPlayer
import com.elleynn.common.utils.messageChain
import com.elleynn.common.utils.sizeToReadable
import com.elleynn.compose.widgets.AppDialog
import com.elleynn.compose.widgets.LoadingContent
import com.elleynn.database.entities.systts.SystemTtsV2
import com.elleynn.database.entities.systts.TtsConfigurationDTO
import com.elleynn.database.entities.systts.source.TextToSpeechSource
import com.elleynn.tts.CachedEngineManager
import com.elleynn.tts.speech.EngineState
import com.elleynn.tts.speech.TextToSpeechProvider
import com.elleynn.tts.synthesizer.SystemParams
import com.elleynn.tts.synthesizer.TtsConfiguration
import com.elleynn.tts.synthesizer.TtsConfiguration.Companion.toVO
import com.elleynn.tts_server_android.R
import com.elleynn.tts_server_android.conf.AppConfig
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException
import splitties.init.appCtx


private val logger = KotlinLogging.logger("AuditionDialog")

@Composable
fun AuditionDialog(
    systts: SystemTtsV2,
    text: String = AppConfig.testSampleText.value,

    config: TtsConfiguration = (systts.config as TtsConfigurationDTO).toVO(),
    engine: TextToSpeechProvider<TextToSpeechSource>? = null,
    onDismissRequest: () -> Unit,
) {
    val context = LocalContext.current
    var error by remember { mutableStateOf("") }
    var info by remember { mutableStateOf("") }
    val audioPlayer = remember { AudioPlayer(context) }

    DisposableEffect(systts) {
        onDispose {
            audioPlayer.stop()
        }
    }

    LaunchedEffect(systts) {
        launch(Dispatchers.IO) {
            try {
                val e = engine ?: CachedEngineManager.getEngine(appCtx, config.source)
                ?: throw IllegalStateException("engine is null")

                if (e.state is EngineState.Uninitialized) e.onInit()
                if (e.isSyncPlay(config.source)) {
                    e.syncPlay(SystemParams(text = text), config.source)
                } else {
                    val stream = e.getStream(SystemParams(text = text), config.source)
                    val audio = stream.readBytes()
                    val rateAndMime =
                        com.elleynn.common.audio.AudioDecoder.getSampleRateAndMime(audio)
                    withMain {
                        info = context.getString(
                            R.string.systts_test_success_info, audio.size.toLong().sizeToReadable(),
                            rateAndMime.first, rateAndMime.second
                        )
                    }

                    if (config.shouldDecode())
                        audioPlayer.play(audio)
                    else
                        audioPlayer.play(audio, config.audioFormat.sampleRate)
                }
                withContext(Dispatchers.Main) {
                    onDismissRequest()
                }
            } catch (e: IOException) {
                error = e.cause.toString()
            } catch (e: Exception) {
                error = e.messageChain
                logger.warn { e.stackTraceToString() }
            }
        }
    }

    AppDialog(onDismissRequest = onDismissRequest,
        title = { Text(stringResource(id = R.string.audition)) },
        content = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                SelectionContainer {
                    Text(
                        error.ifEmpty { text },
                        color = if (error.isEmpty()) Color.Unspecified else MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                if (error.isEmpty())
                    LoadingContent(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth(),
                        isLoading = info.isEmpty()
                    ) {
                        SelectionContainer {
                            Text(info, style = MaterialTheme.typography.bodyMedium)
                        }
                    }

            }
        },
        buttons = {
            TextButton(onClick = onDismissRequest) { Text(stringResource(id = R.string.cancel)) }
        }
    )

}