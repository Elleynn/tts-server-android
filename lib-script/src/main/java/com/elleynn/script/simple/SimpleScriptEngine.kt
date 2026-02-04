package com.elleynn.script.simple

import android.content.Context
import com.elleynn.script.engine.RhinoScriptEngine
import com.elleynn.script.simple.ext.JsExtensions

class SimpleScriptEngine(context: Context, id: String) :
    RhinoScriptEngine(CompatScriptRuntime(JsExtensions(context, id))) {
}