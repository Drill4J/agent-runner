plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(gradleApi())
    compileOnly(kotlin("stdlib-jdk8"))
    compileOnly(kotlin("gradle-plugin"))
    compileOnly(project(":common"))

    testCompileOnly(kotlin("test"))
    testCompileOnly(kotlin("test-junit"))
    testCompileOnly(gradleTestKit())
}

sourceSets {
    val main: SourceSet by getting
    main.java.srcDirs("../common/src/main")
}
tasks {

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
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
