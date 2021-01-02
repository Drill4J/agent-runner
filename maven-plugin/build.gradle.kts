import java.lang.System.out

plugins {
    kotlin("jvm")
    `maven-publish`
}

val kotlinVersion: String by extra

dependencies {
    implementation(project(":common"))
    implementation(kotlin("stdlib"))
    implementation("org.apache.maven:maven-core:3.5.3")
    implementation("org.apache.maven:maven-plugin-api:3.5.3")
    implementation("org.apache.maven.plugin-tools:maven-plugin-annotations:3.5")
    implementation("org.twdata.maven:mojo-executor:2.3.0")
    implementation("org.apache.maven.plugins:maven-surefire-plugin:2.22.1")
}

val install by tasks.registering(Exec::class) {
    workingDir(project.projectDir)
    val isWindows = System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0
    val args = if (isWindows) arrayOf("cmd", "/c", "mvnw.cmd") else arrayOf("sh", "./mvnw")
    commandLine(*args, "install", "-Ddrill.plugin.version=$version", "-Dkotlin.version=$kotlinVersion")
    standardOutput = out
}

publishing {
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
                            appendNode("version", kotlinVersion)
                        }
                    }
                }
            }
        }
    }
}
tasks {
    publish {
        dependsOn(install)
    }

    publishToMavenLocal {
        dependsOn(install)
    }
}
