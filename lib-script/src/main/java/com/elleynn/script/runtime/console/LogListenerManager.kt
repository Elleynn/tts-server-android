package com.elleynn.script.runtime.console

import android.util.Log.ASSERT
import android.util.Log.DEBUG
import android.util.Log.ERROR
import android.util.Log.INFO
import android.util.Log.VERBOSE
import android.util.Log.WARN
import com.elleynn.common.LogEntry

interface LogListenerManager {
    fun addLogListener(listener: LogListener)
    fun removeLogListener(listener: LogListener)
}

fun interface LogListener {
    fun onNewLog(entry: LogEntry)

}


