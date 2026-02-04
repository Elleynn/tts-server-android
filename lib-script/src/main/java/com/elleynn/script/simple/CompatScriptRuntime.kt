package com.elleynn.script.simple

import com.elleynn.script.runtime.Environment
import com.elleynn.script.runtime.RhinoScriptRuntime
import com.elleynn.script.simple.ext.JsExtensions

class CompatScriptRuntime(val ttsrv: JsExtensions) :
    RhinoScriptRuntime(
        environment = Environment(
            ttsrv.context.externalCacheDir?.absolutePath
                ?: throw IllegalArgumentException("context.externalCacheDir is null"),
            ttsrv.engineId
        )
    ) {
    override fun init() {
        super.init()
        globalScope.defineGetter("ttsrv", ::ttsrv)
    }
}