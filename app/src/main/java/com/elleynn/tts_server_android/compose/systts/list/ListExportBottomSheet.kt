package com.elleynn.tts_server_android.compose.systts.list

import androidx.compose.runtime.Composable
import com.elleynn.tts_server_android.compose.systts.ConfigExportBottomSheet
import com.elleynn.tts_server_android.constant.AppConst
import com.elleynn.database.entities.systts.GroupWithSystemTts
import kotlinx.serialization.encodeToString

@Composable
fun ListExportBottomSheet(onDismissRequest: () -> Unit, list: List<GroupWithSystemTts>) {
    ConfigExportBottomSheet(
        json = AppConst.jsonBuilder.encodeToString(list),
        onDismissRequest = onDismissRequest,
        fileName = "ttsrv-list.json"
    )
}