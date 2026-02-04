package com.elleynn.script

abstract class JavaScriptEngine : ScriptEngine {
    open fun put(key: String, value: Any?) {}
    open fun get(key: String): Any? = null
}