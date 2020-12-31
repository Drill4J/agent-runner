rootProject.name = "agent-runner"
include(":common")
include(":gradle-plugin")
include(":maven-plugin")

val scriptUrl: String by extra
apply(from = "$scriptUrl/maven-repo.settings.gradle.kts")

pluginManagement {
    val kotlinVersion: String by extra
    plugins {
        kotlin("jvm") version kotlinVersion
    }
}
