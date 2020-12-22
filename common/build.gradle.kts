plugins {
    kotlin("jvm")
    maven
    `maven-publish`
}

repositories {
    mavenCentral()
}
val kotlinVersion: String by extra

dependencies {
    implementation(kotlin("stdlib", kotlinVersion))
}
