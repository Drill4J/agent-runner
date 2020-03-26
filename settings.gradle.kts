rootProject.name = "agent-runner"
include(":gradle-plugin")
include(":maven-plugin")
pluginManagement {
    repositories {
        mavenLocal()
        maven(url = "http://oss.jfrog.org/oss-release-local")
        gradlePluginPortal()
    }
}