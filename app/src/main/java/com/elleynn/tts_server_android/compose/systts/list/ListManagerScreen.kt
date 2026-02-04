package com.elleynn.tts_server_android.compose.systts.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.drake.net.utils.withIO
import com.elleynn.common.utils.longToast
import com.elleynn.common.utils.toast
import com.elleynn.compose.widgets.ControlBottomBarVisibility
import com.elleynn.compose.widgets.LazyListIndexStateSaver
import com.elleynn.compose.widgets.ShadowedDraggableItem
import com.elleynn.compose.widgets.TextFieldDialog
import com.elleynn.database.dbm
import com.elleynn.database.entities.AbstractListGroup
import com.elleynn.database.entities.systts.BgmConfiguration
import com.elleynn.database.entities.systts.GroupWithSystemTts
import com.elleynn.database.entities.systts.SystemTtsGroup
import com.elleynn.database.entities.systts.SystemTtsV2
import com.elleynn.database.entities.systts.TtsConfigurationDTO
import com.elleynn.database.entities.systts.source.LocalTtsSource
import com.elleynn.database.entities.systts.source.PluginTtsSource
import com.elleynn.tts_server_android.AppLocale
import com.elleynn.tts_server_android.R
import com.elleynn.tts_server_android.compose.AppDefaultProperties
import com.elleynn.tts_server_android.compose.LocalBottomBarBehavior
import com.elleynn.tts_server_android.compose.LocalNavController
import com.elleynn.tts_server_android.compose.SharedViewModel
import com.elleynn.tts_server_android.compose.nav.NavRoutes
import com.elleynn.tts_server_android.compose.nav.NavTopAppBar
import com.elleynn.tts_server_android.compose.systts.AuditionDialog
import com.elleynn.tts_server_android.compose.systts.ConfigDeleteDialog
import com.elleynn.tts_server_android.compose.systts.ConfigExportBottomSheet
import com.elleynn.tts_server_android.compose.systts.list.ui.ItemDescriptorFactory
import com.elleynn.tts_server_android.compose.systts.list.ui.widgets.QuickEditBottomSheet
import com.elleynn.tts_server_android.compose.systts.list.ui.widgets.TagDataClearConfirmDialog
import com.elleynn.tts_server_android.compose.systts.plugin.PluginSelectionDialog
import com.elleynn.tts_server_android.compose.systts.sizeToToggleableState
import com.elleynn.tts_server_android.constant.AppConst
import com.elleynn.tts_server_android.constant.SpeechTarget
import com.elleynn.tts_server_android.model.rhino.speech_rule.SpeechRuleEngine
import com.elleynn.tts_server_android.service.systts.SystemTtsService
import com.elleynn.tts_server_android.toCode
import com.elleynn.tts_server_android.ui.view.AppDialogs.displayErrorDialog
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun ListManagerScreen(
    sharedVM: SharedViewModel,
    vm: ListManagerViewModel = viewModel(),
) {
    val navController = LocalNavController.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var showSortDialog by remember { mutableStateOf<List<SystemTtsV2>?>(null) }
    if (showSortDialog != null) SortDialog(
        onDismissRequest = { showSortDialog = null },
        list = showSortDialog!!
    )

    var showQuickEdit by remember { mutableStateOf<SystemTtsV2?>(null) }
    if (showQuickEdit != null) {
        QuickEditBottomSheet(onDismissRequest = {
            dbm.systemTtsV2.insert(showQuickEdit!!)
            if (showQuickEdit?.isEnabled == true) SystemTtsService.notifyUpdateConfig()
            showQuickEdit = null
        }, systts = showQuickEdit!!, onSysttsChange = {
            showQuickEdit = it
        })
    }

    fun navigateToEdit(systts: SystemTtsV2) {
        sharedVM.put(NavRoutes.TtsEdit.DATA, systts)
        navController.navigate(NavRoutes.TtsEdit.id)
    }

    // 长按Item拖拽提示
    var hasShownTip by rememberSaveable { mutableStateOf(false) }

    var showTagClearDialog by remember { mutableStateOf<SystemTtsV2?>(null) }
    if (showTagClearDialog != null) {
        val systts = showTagClearDialog!!
        val config = systts.config as TtsConfigurationDTO
        TagDataClearConfirmDialog(
            tagData = config.speechRule.tagData.toString(),
            onDismissRequest = { showTagClearDialog = null },
            onConfirm = {
                dbm.systemTtsV2.update(
                    systts.copy(
                        config = config.copy(
                            speechRule = config.speechRule.copy(
                                target = SpeechTarget.ALL,
                            ).apply { resetTag() },
                        )
                    )
                )
                if (systts.isEnabled) SystemTtsService.notifyUpdateConfig()
                showTagClearDialog = null
            }
        )
    }

    fun switchSpeechTarget(systts: SystemTtsV2) {
        if (!hasShownTip) {
            hasShownTip = true
            context.longToast(R.string.systts_drag_tip_msg)
        }

        val config = systts.config as TtsConfigurationDTO
        if (config.speechRule.target == SpeechTarget.BGM) return
        val ruleData = config.speechRule.copy()

        if (config.speechRule.target == SpeechTarget.TAG) dbm.speechRuleDao.getByRuleId(
            config.speechRule.tagRuleId
        )?.let { speechRule ->
            val keys = speechRule.tags.keys.toList()
            val idx = keys.indexOf(config.speechRule.tag)

            val nextIndex = (idx + 1)
            val newTag = keys.getOrNull(nextIndex)
            if (newTag == null) {
                if (ruleData.isTagDataEmpty()) {
                    ruleData.target = SpeechTarget.ALL
                    ruleData.resetTag()
                } else {
                    showTagClearDialog = systts
                    return
                }
            } else {
                ruleData.tag = newTag
                runCatching {
                    ruleData.tagName =
                        SpeechRuleEngine.getTagName(context, speechRule, info = ruleData)
                }.onFailure {
                    ruleData.tagName = ""
                    context.displayErrorDialog(it)
                }

            }
        }
        else {
            dbm.speechRuleDao.getByRuleId(ruleData.tagRuleId)?.let {
                ruleData.target = SpeechTarget.TAG
                ruleData.tag = it.tags.keys.first()
            }
        }

        dbm.systemTtsV2.update(systts.copy(config = systts.ttsConfig.copy(speechRule = ruleData)))
        if (systts.isEnabled) SystemTtsService.notifyUpdateConfig()
    }

    var deleteTts by remember { mutableStateOf<SystemTtsV2?>(null) }
    if (deleteTts != null) {
        ConfigDeleteDialog(
            onDismissRequest = { deleteTts = null }, content = deleteTts?.displayName ?: ""
        ) {
            dbm.systemTtsV2.delete(deleteTts!!)
            deleteTts = null
        }
    }

    var groupAudioParamsDialog by remember { mutableStateOf<SystemTtsGroup?>(null) }
    if (groupAudioParamsDialog != null) {
        GroupAudioParamsDialog(onDismissRequest = { groupAudioParamsDialog = null },
            params = groupAudioParamsDialog!!.audioParams,
            onConfirm = {
                dbm.systemTtsV2.updateGroup(
                    groupAudioParamsDialog!!.copy(audioParams = it)
                )

                groupAudioParamsDialog = null
            })
    }

    val models by vm.list.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    LazyListIndexStateSaver(models = models, listState = listState)

    val reorderState = rememberReorderableLazyListState(
        listState = listState, onMove = vm::reorder
    )

    var addGroupDialog by remember { mutableStateOf(false) }
    if (addGroupDialog) {
        var name by remember { mutableStateOf("") }
        TextFieldDialog(title = stringResource(id = R.string.add_group),
            text = name,
            onTextChange = { name = it },
            onDismissRequest = { addGroupDialog = false }) {
            addGroupDialog = false
            dbm.systemTtsV2.insertGroup(SystemTtsGroup(name = name))
        }
    }

    var showGroupExportSheet by remember { mutableStateOf<List<GroupWithSystemTts>?>(null) }
    if (showGroupExportSheet != null) {
        val list = showGroupExportSheet!!
        ListExportBottomSheet(onDismissRequest = { showGroupExportSheet = null }, list = list)
    }

    var showExportSheet by remember { mutableStateOf<List<SystemTtsV2>?>(null) }
    if (showExportSheet != null) {
        val jStr = remember { AppConst.jsonBuilder.encodeToString(showExportSheet!!) }
        ConfigExportBottomSheet(json = jStr) { showExportSheet = null }
    }

    var addPluginDialog by remember { mutableStateOf(false) }
    if (addPluginDialog) {
        PluginSelectionDialog(onDismissRequest = { addPluginDialog = false }) {
            navigateToEdit(
                SystemTtsV2(
                    config = TtsConfigurationDTO(
                        source = PluginTtsSource(
                            pluginId = it.pluginId,
                            locale = AppLocale.current(context).toCode()
                        )
                    )
                )
            )
        }
    }

    var showAuditionDialog by remember { mutableStateOf<SystemTtsV2?>(null) }
    if (showAuditionDialog != null) AuditionDialog(systts = showAuditionDialog!!) {
        showAuditionDialog = null
    }

    var showOptions by rememberSaveable { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            NavTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(stringResource(id = R.string.system_tts))
                }, actions = {
                    IconButton(onClick = { showOptions = true }) {
                        Icon(Icons.Default.MoreVert, stringResource(id = R.string.more_options))
                        MenuMoreOptions(
                            expanded = showOptions,
                            onDismissRequest = { showOptions = false },
                            onExportAll = { showGroupExportSheet = models },
                        )
                    }
                })
        },
    ) { paddingValues ->
        Box(Modifier.padding(top = paddingValues.calculateTopPadding())) {
            ControlBottomBarVisibility(listState, LocalBottomBarBehavior.current)
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .reorderable(state = reorderState),
                state = listState
            ) {
                models.forEachIndexed { _, groupWithSystemTts ->
                    val g = groupWithSystemTts.group
                    val checkState =
                        groupWithSystemTts.list.filter { it.isEnabled }.size.sizeToToggleableState(
                            groupWithSystemTts.list.size
                        )
                    val key = "g_${g.id}"
                    stickyHeader(key = key) {
                        ShadowedDraggableItem(reorderableState = reorderState, key = key) {
                            Group(modifier = Modifier.detectReorderAfterLongPress(reorderState),
                                name = g.name,
                                isExpanded = g.isExpanded,
                                toggleableState = checkState,
                                onToggleableStateChange = {
                                    vm.updateGroupEnable(groupWithSystemTts, it)
                                },
                                onClick = {
                                    dbm.systemTtsV2.updateGroup(g.copy(isExpanded = !g.isExpanded))
                                },
                                onDelete = {
                                    dbm.systemTtsV2.delete(*groupWithSystemTts.list.toTypedArray())
                                    dbm.systemTtsV2.deleteGroup(g)
                                },
                                onRename = {
                                    dbm.systemTtsV2.updateGroup(g.copy(name = it))
                                },
                                onCopy = {
                                    scope.launch {
                                        val group = g.copy(id = System.currentTimeMillis(),
                                            name = it.ifBlank { context.getString(R.string.unnamed) })
                                        dbm.systemTtsV2.insertGroup(group)
                                        dbm.systemTtsV2.getByGroup(g.id)
                                            .forEachIndexed { index, tts ->
                                                dbm.systemTtsV2.insert(
                                                    tts.copy(
                                                        id = System.currentTimeMillis() + index,
                                                        groupId = group.id
                                                    )
                                                )
                                            }
                                    }
                                },
                                onEditAudioParams = {
                                    groupAudioParamsDialog = g
                                },
                                onExport = {
                                    showGroupExportSheet = listOf(groupWithSystemTts)
                                },
                                onSort = {
                                    showSortDialog = groupWithSystemTts.list
                                }
                            )
                        }
                    }

                    if (g.isExpanded) {
                        itemsIndexed(groupWithSystemTts.list.sortedBy { it.order },
                            key = { _, v -> "${g.id}_${v.id}" }) { _, item ->
                            if (g.id == 1L) println(item.displayName + ", " + item.order)

                            ShadowedDraggableItem(
                                reorderableState = reorderState,
                                key = "${g.id}_${item.id}"
                            ) {
                                val descriptor = remember(item) {
                                    ItemDescriptorFactory.from(context, item)
                                }
                                Item(reorderState = reorderState,
                                    modifier = Modifier.padding(
                                        horizontal = 8.dp,
                                        vertical = 4.dp
                                    ),
                                    name = item.displayName,
                                    tagName = descriptor.tagName,
                                    type = descriptor.type,
                                    standby = descriptor.standby,
                                    enabled = item.isEnabled,
                                    onEnabledChange = {
                                        vm.updateTtsEnabled(item, it)
                                        if (it) SystemTtsService.notifyUpdateConfig()
                                    },
                                    desc = descriptor.desc,
                                    params = descriptor.bottom,
                                    onClick = { showQuickEdit = item },
                                    onLongClick = { switchSpeechTarget(item) },
                                    onCopy = {
                                        navigateToEdit(item.copy(id = System.currentTimeMillis()))
                                    },
                                    onDelete = { deleteTts = item },
                                    onEdit = { navigateToEdit(item) },
                                    onAudition = {
                                        if (item.config is TtsConfigurationDTO) {
                                            showAuditionDialog = item
                                        } else
                                            context.toast(R.string.not_support_audition)
                                    },
                                    onExport = {
                                        showExportSheet =
                                            listOf(item.copy(groupId = AbstractListGroup.DEFAULT_GROUP_ID))
                                    }
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(Modifier.padding(bottom = AppDefaultProperties.LIST_END_PADDING))
                }
            }

            FloatingAddConfigButtonGroup(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp),
                visible = true,
                addBgm = {
                    navigateToEdit(SystemTtsV2(config = BgmConfiguration()))
                },
                addLocal = {
                    navigateToEdit(
                        SystemTtsV2(
                            config = TtsConfigurationDTO(
                                source = LocalTtsSource(locale = AppConst.localeCode)
                            )
                        )
                    )
                },
                addPlugin = {
                    addPluginDialog = true
                },
                addGroup = {
                    addGroupDialog = true
                }
            )

            LaunchedEffect(key1 = Unit) {
                withIO {
                    vm.checkListData(context)
                }
            }

        }
    }
}