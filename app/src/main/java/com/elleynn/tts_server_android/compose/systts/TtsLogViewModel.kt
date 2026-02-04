package com.elleynn.tts_server_android.compose.systts

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drake.net.utils.withMain
import com.elleynn.common.LogEntry
import com.elleynn.common.LogLevel
import com.elleynn.common.toLogLevel
import com.elleynn.common.utils.runOnUI
import com.elleynn.tts_server_android.SysttsLogger
import com.elleynn.tts_server_android.constant.AppConst
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter

class TtsLogViewModel : ViewModel() {
    companion object {
        const val TAG = "TtsLogViewModel"

        const val MAX_SIZE = 150

        private val logger = KotlinLogging.logger(TAG)
        val file =
            File(AppConst.externalFilesDir.absolutePath + File.separator + "log" + File.separator + "system_tts.log")
    }

    val logs = mutableStateListOf<LogEntry>()

    fun clear() {
        logs.clear()
        runCatching {
            FileWriter(file, false).use { it.write(CharArray(0)) }
        }.onFailure {
            logs.add(LogEntry(level = LogLevel.ERROR, message = it.stackTraceToString()))
        }
    }


    private fun toLogEntry(line: String): LogEntry {
        return line.split(" | ").let {
            val time = it[0]
            val level = it[1]
            val msg = it[2]
            LogEntry(
                level = level.toLogLevel(), time = time, message = msg
            )
        }
    }

    fun logDir(): String {
        return file.absolutePath
    }

    init {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                pull()
                SysttsLogger.register({ log ->
                    runOnUI {
                        logs.add(log)
                    }
                })
            }
        } catch (e: Exception) {
        }
    }

    fun add(line: String) {
        try {
            val logEntry = toLogEntry(line)
            if (logs.size > MAX_SIZE)
                logs.removeRange(0, 10)
            logs.add(logEntry)

        } catch (e: Exception) {
        }
    }

    @Suppress("DEPRECATION")
    suspend fun pull() {
        runCatching {
            file.readLines().takeLast(MAX_SIZE).apply {
                withMain {
                    forEach { add(it) }
                }
            }
        }.onFailure {
            logs.add(LogEntry(level = LogLevel.ERROR, message = it.stackTraceToString()))
        }

    }
}