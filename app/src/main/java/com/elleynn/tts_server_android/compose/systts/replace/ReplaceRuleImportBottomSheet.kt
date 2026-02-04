package com.elleynn.tts_server_android.compose.systts.replace

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
import com.elleynn.database.entities.replace.GroupWithReplaceRule
import com.elleynn.database.entities.replace.ReplaceRule
import com.elleynn.database.entities.replace.ReplaceRuleGroup
import com.elleynn.common.utils.StringUtils
import com.elleynn.common.utils.toJsonListString

@Suppress("UNCHECKED_CAST")
@Composable
fun ReplaceRuleImportBottomSheet(onDismissRequest: () -> Unit) {
    var list by remember { mutableStateOf<List<ConfigModel>?>(null) }
    if (list != null) {
        SelectImportConfigDialog(
            onDismissRequest = { list = null },
            models = list!!,
            onSelectedList = { selectedList ->
                selectedList.map { it as Pair<ReplaceRuleGroup, ReplaceRule> }.forEach {
                    val group = it.first
                    val rule = it.second

                    dbm.replaceRuleDao.insert(rule)
                    dbm.replaceRuleDao.insertGroup(group)
                }
                dbm.replaceRuleDao.updateAllOrder()
                selectedList.size
            }
        )
    }

    ConfigImportBottomSheet(onDismissRequest = onDismissRequest, onImport = { json ->
        val allList = mutableListOf<ConfigModel>()
        if (json.contains("\"group\"")) {
            AppConst.jsonBuilder.decodeFromString<List<GroupWithReplaceRule>>(json.toJsonListString())
                .forEach { groupWithRule ->
                    val group = groupWithRule.group
                    groupWithRule.list.forEach {
                        allList.add(
                            ConfigModel(
                                isSelected = true,
                                title = it.name,
                                subtitle = group.name,
                                data = Pair(group, it)
                            )
                        )
                    }
                }

        } else {
            val groupName = StringUtils.formattedDate()
            val group = ReplaceRuleGroup(name = groupName)
            AppConst.jsonBuilder.decodeFromString<List<ReplaceRule>>(json.toJsonListString())
                .forEach {
                    allList.add(
                        ConfigModel(
                            isSelected = true, title = it.name, subtitle = groupName,
                            data = Pair(group, it.apply { groupId = group.id })
                        )
                    )
                }
        }

        list = allList
    })
}