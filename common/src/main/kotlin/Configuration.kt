/**
 * Copyright 2020 EPAM Systems
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.epam.drill.agent.runner

import java.io.File

abstract class Configuration {
    lateinit var adminHost: String
    var agentId: String? = null
    var groupId: String? = null
    var adminPort: Int = 8080
    var version: String = "+"
    var agentPath: File? = null
    var runtimePath: File? = null
    var logLevel: LogLevels = LogLevels.ERROR
    var logFile: File? = null
    var additionalParams: Map<String, String>? = null
    var jvmArgs: Set<String> = mutableSetOf()
    abstract val artifactGroup: String
    abstract val artifactId: String
    fun toJvmArgs(): List<String> {
        val args = mutableMapOf<String, Any?>().apply {
            this["drillInstallationDir"] = runtimePath
            this["adminAddress"] = "$adminHost:$adminPort"
            this["agentId"] = agentId
            this["logLevel"] = logLevel.name
            if (groupId != null) this["groupId"] = groupId!!
            if (logFile != null) this["logFile"] = logFile!!.absolutePath
            if (additionalParams != null) {
                this.putAll(additionalParams!!)
            }
            putAll(jvmArgs())
        }
        return jvmArgs.toList() + ("-agentpath:${agentPath}=" + args.map { (k, v) -> "$k=$v" }.joinToString(","))
    }

    abstract fun jvmArgs(): Map<String, String>
}
