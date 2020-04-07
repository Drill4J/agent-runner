package com.epam.drill.agent.runner

import java.io.File

abstract class Configuration {
    lateinit var adminHost: String
    var agentId: String? = null
    var adminPort: Int = 8080
    var version: String = "+"
    var agentPath: File? = null
    var runtimePath: File? = null
    abstract val artifactGroup: String
    abstract val artifactId: String
    abstract fun toJvmArgs(): String
}
