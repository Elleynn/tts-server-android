package com.elleynn.tts_server_android.compose.systts.plugin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.elleynn.tts_server_android.compose.systts.ConfigImportBottomSheet
import com.elleynn.tts_server_android.compose.systts.ConfigModel
import com.elleynn.tts_server_android.compose.systts.SelectImportConfigDialog
import com.elleynn.tts_server_android.constant.AppConst
import com.elleynn.database.dbm
import com.elleynn.database.entities.plugin.Plugin

@Composable
fun PluginImportBottomSheet(onDismissRequest: () -> Unit) {
    var list by remember { mutableStateOf<List<Plugin>?>(null) }
    if (list != null) {
        SelectImportConfigDialog(
            onDismissRequest = { list = null },
            models = list!!.map {
                ConfigModel(
                    isSelected = true,
                    title = it.name,
                    subtitle = it.author,
                    it
                )
            },
            onSelectedList = {
                dbm.pluginDao.insert(*it.map { plugin -> plugin as Plugin }.toTypedArray())

                it.size
            }
        )
    }

    ConfigImportBottomSheet(onDismissRequest = onDismissRequest, onImport = {
        list = AppConst.jsonBuilder.decodeFromString<List<Plugin>>(it)
    })
}