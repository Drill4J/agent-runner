plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

dependencies {
    compileOnly(gradleApi())
    compileOnly((kotlin("stdlib-jdk8")))
    compileOnly((kotlin("gradle-plugin")))
    implementation(project(":common"))
}

gradlePlugin {
    plugins {
        create("autotest") {
            id = "$group.autotest"
            implementationClass = "com.epam.drill.autotest.gradle.AutoTestAgent"
        }
        create("app") {
            id = "$group.app"
            implementationClass = "com.epam.drill.autotest.gradle.AppAgent"
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("jvm") {
            from(components["java"])
        }
    }
}
