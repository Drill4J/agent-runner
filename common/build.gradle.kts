plugins {
    kotlin("jvm") version ("1.3.70")
    maven
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
}
