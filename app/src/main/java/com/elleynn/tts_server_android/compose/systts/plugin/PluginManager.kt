package com.elleynn.tts_server_android.compose.systts.plugin

import com.elleynn.database.entities.plugin.Plugin
import com.elleynn.tts_server_android.constant.AppConst
import java.io.File

class PluginManager(private val plugin: Plugin) {
    private val cacheDir = File(AppConst.externalCacheDir.absolutePath + "/${plugin.pluginId}")
    fun hasCache(): Boolean {
        return try {
            cacheDir.list()?.isNotEmpty() == true
        } catch (e: Exception) {
            false
        }
    }

    fun clearCache() {
        try {
            cacheDir.deleteRecursively()
        } catch (_: Exception) {
        }
    }
}