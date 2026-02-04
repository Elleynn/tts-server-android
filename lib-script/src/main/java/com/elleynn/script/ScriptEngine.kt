package com.elleynn.script

import com.elleynn.script.source.ScriptSource

interface ScriptEngine {
    fun init()
    fun destroy()
    fun execute(source: ScriptSource): Any?
}