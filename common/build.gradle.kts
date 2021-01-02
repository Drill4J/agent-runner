plugins {
    kotlin("jvm")
    `maven-publish`
}

dependencies {
    compileOnly(kotlin("stdlib-jdk8"))
}

publishing {
    publications {
        create<MavenPublication>("jvm") {
            from(components["java"])
        }
    }
}
