package com.elleynn.tts.speech.plugin.engine

object PackageImporter {
    val default by lazy {
        line(
            listOf(
                "com.elleynn.tts.speech.plugin.engine.type.ws",
                "com.elleynn.tts.speech.plugin.engine.type.ui",
                "android.view",
                "android.widget",
            )
        )
    }

    private fun line(packages: List<String>): String {
        val s = packages.joinToString(separator = ";") { "importPackage($it)" }
        return "$s;"
    }
}