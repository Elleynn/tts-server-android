package com.elleynn.server.forwarder

import kotlinx.serialization.Serializable

@Serializable
data class Engine(val name: String, val label: String)