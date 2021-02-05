rootProject.name = "agent-runner"

val scriptUrl: String by extra
apply(from = "$scriptUrl/maven-repo.settings.gradle.kts")

pluginManagement {
    val kotlinVersion: String by extra
    val licenseVersion: String by extra
    plugins {
        kotlin("jvm") version kotlinVersion
        id("com.github.hierynomus.license") version licenseVersion
    }
}

include(":common")
include(":gradle-plugin")
include(":maven-plugin")
