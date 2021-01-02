plugins {
    base
    kotlin("jvm") apply false
}

val scriptUrl: String by extra

allprojects {
    apply(from = "$scriptUrl/git-version.gradle.kts")
}

subprojects {
    repositories {
        mavenLocal()
        apply(from = "$scriptUrl/maven-repo.gradle.kts")
        jcenter()
    }

    plugins.withType<JavaBasePlugin>() {
        the<JavaPluginConvention>().targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>() {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}
