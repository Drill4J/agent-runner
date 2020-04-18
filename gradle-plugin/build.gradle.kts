plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

repositories {
    mavenCentral()
}

val kotlinVersion = "1.3.70"
dependencies {
    implementation(gradleApi())
    implementation(kotlin("stdlib-jdk8", kotlinVersion))
    implementation(kotlin("gradle-plugin", kotlinVersion))
    compileOnly(project(":common"))

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
    testImplementation(gradleTestKit())
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

publishing {
    repositories {
        maven {
            url = uri("http://oss.jfrog.org/oss-release-local")
            credentials {
                username =
                    if (project.hasProperty("bintrayUser"))
                        project.property("bintrayUser").toString()
                    else System.getenv("BINTRAY_USER")
                password =
                    if (project.hasProperty("bintrayApiKey"))
                        project.property("bintrayApiKey").toString()
                    else System.getenv("BINTRAY_API_KEY")
            }
        }
    }
}
