plugins {
    kotlin("jvm")
    maven
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib")
}
