import java.lang.System.out

plugins {
    kotlin("jvm") version ("1.3.70")
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.apache.maven:maven-core:3.5.3")
    implementation("org.apache.maven:maven-plugin-api:3.5.3")
    implementation("org.apache.maven.plugin-tools:maven-plugin-annotations:3.5")
    implementation("org.twdata.maven:mojo-executor:2.3.0")
    implementation("org.apache.maven.plugins:maven-surefire-plugin:2.22.1")
}

tasks {

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}

val install by tasks.creating(Exec::class) {
    workingDir(project.projectDir)
    val isWindows = System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0
    val args = if (isWindows) arrayOf("cmd", "/c", "mvnw.cmd") else arrayOf("sh", "./mvnw")
    commandLine(*args, "install")
    standardOutput = out
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
    publications {
        create<MavenPublication>("drill-maven") {
            groupId = "com.epam.drill.agent.runner"
            artifactId = "maven"
            artifact(file("target/maven-$version.jar"))
            pom.withXml {
                asNode().apply {

                    appendNode("dependencies").apply {
                        appendNode("dependency").apply {
                            appendNode("groupId", "org.jetbrains.kotlin")
                            appendNode("artifactId", "kotlin-stdlib")
                            appendNode("version", "1.3.71")
                        }
                    }
                }
            }
        }
    }
}
tasks {
    "publish"{
        dependsOn("install")
    }

    "publishToMavenLocal"{
        dependsOn("install")
    }
}