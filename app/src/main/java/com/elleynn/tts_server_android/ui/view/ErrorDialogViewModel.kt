package com.elleynn.tts_server_android.ui.view

import androidx.lifecycle.ViewModel

class ErrorDialogViewModel : ViewModel() {
    internal val throwableList = mutableMapOf<String, Throwable>()
}