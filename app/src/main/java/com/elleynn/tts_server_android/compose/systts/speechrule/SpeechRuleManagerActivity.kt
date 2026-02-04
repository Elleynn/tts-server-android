package com.elleynn.tts_server_android.compose.systts.speechrule

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.elleynn.database.dbm
import com.elleynn.database.entities.SpeechRule
import com.elleynn.tts_server_android.compose.ComposeActivity
import com.elleynn.tts_server_android.compose.LocalNavController
import com.elleynn.tts_server_android.compose.SharedViewModel
import com.elleynn.tts_server_android.compose.theme.AppTheme

class SpeechRuleManagerActivity : ComposeActivity() {
    private var jsCode by mutableStateOf("")

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent != null) importJsCodeFromIntent(intent)

        setContent {
            AppTheme {
                val navController = rememberNavController()
                val sharedVM: SharedViewModel = viewModel()
                CompositionLocalProvider(LocalNavController provides navController) {
                    LaunchedEffect(jsCode) {
                        if (jsCode.isNotBlank()) {
                            sharedVM.put(
                                NavRoutes.SpeechRuleEdit.KEY_DATA, SpeechRule(code = jsCode)
                            )
                            navController.navigate(NavRoutes.SpeechRuleEdit.id)
                        }
                    }

                    NavHost(
                        navController = navController,
                        startDestination = NavRoutes.SpeechRuleManager.id
                    ) {
                        composable(NavRoutes.SpeechRuleManager.id) {
                            SpeechRuleManagerScreen(sharedVM) { finishAfterTransition() }
                        }

                        composable(NavRoutes.SpeechRuleEdit.id) {
                            val rule = rememberSaveable {
                                sharedVM.getOnce<SpeechRule>(NavRoutes.SpeechRuleEdit.KEY_DATA)
                                    ?: SpeechRule()
                            }
                            SpeechRuleEditScreen(rule, onSave = {
                                dbm.speechRuleDao.insert(it)
                            })
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        importJsCodeFromIntent(intent)
    }


    private fun importJsCodeFromIntent(intent: Intent) {
        jsCode = intent.getStringExtra("js") ?: return
        intent.removeExtra("js")
    }
}