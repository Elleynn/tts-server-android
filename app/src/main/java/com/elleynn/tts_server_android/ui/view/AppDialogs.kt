package com.elleynn.tts_server_android.ui.view

import android.content.Context
import com.elleynn.common.utils.runOnUI
import com.elleynn.tts_server_android.R

object AppDialogs {
    fun Context.displayErrorDialog(t: Throwable, title: String = getString(R.string.error)) {
        runOnUI {
            ErrorDialogActivity.start(this, title, t = t)
        }
    }
}