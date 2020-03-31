package com.epam.drill.agent.runner

import java.util.*

object OS {
    const val FAMILY_MAC = "mac"
    const val FAMILY_WINDOWS = "windows"
    const val FAMILY_UNIX = "unix"

    private var OS_NAME: String = System.getProperty("os.name").toLowerCase(Locale.ENGLISH)


    fun isFamily(st: String): Boolean {
        return OS_NAME.contains(st)
    }
}


val presetName: String =
    when {
        OS.isFamily(OS.FAMILY_MAC) -> "macosX64"
        OS.isFamily(OS.FAMILY_WINDOWS) -> "mingwX64"
        else -> "linuxX64"
    }

val dynamicLibExtensions = setOf(
    "dylib",
    "so",
    "dll",
    "wasm"
)