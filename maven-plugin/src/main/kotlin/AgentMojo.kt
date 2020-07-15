import com.epam.drill.agent.runner.Configuration
import com.epam.drill.agent.runner.dynamicLibExtensions
import com.epam.drill.agent.runner.presetName
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
import java.io.File
import java.net.URL
import java.util.*
import java.util.zip.ZipFile


const val ARTIFACTORY_URL = "https://oss.jfrog.org/artifactory"
const val REPOSITORY_NAME = "oss-release-local"

abstract class AgentMojo : AbstractMojo() {

    @Parameter(defaultValue = "\${project}", required = true, readonly = true)
    lateinit var project: MavenProject

    abstract val config: Configuration

    override fun execute() {
        val name = "argLine"
        prepareAgent(config)
        val projectProperties: Properties = project.properties
        projectProperties.forEach { x, y ->
            println("$x: $y")
        }
        val oldValue = projectProperties.getProperty(name)
        val newValue: String = "${oldValue ?: ""} ${config.toJvmArgs().joinToString(separator = " ")}".trim()
        log.info("$name set to $newValue")
        projectProperties.setProperty(name, newValue)
    }

    private fun prepareAgent(ac: Configuration) {
        val dir = File(project.build.outputDirectory).parentFile
            .resolve("drill")
            .apply { mkdirs() }
        if (ac.version == "+") {
            ac.version =
                URL("$ARTIFACTORY_URL/api/search/latestVersion?g=${ac.artifactGroup}&a=${ac.artifactId}-$presetName&v=+&repos=$REPOSITORY_NAME").readText()
                    .lines().first()
            println("version is ${ac.version}")

        }
        val artName = "${ac.artifactId}-$presetName-${ac.version}"
        val artFileName = "$artName.zip"
        if (!dir.resolve("$presetName-${ac.version}").exists()) {
            val groupToUrlPath = ac.artifactGroup.replace(".", "/")
            val url =
                URL("$ARTIFACTORY_URL/list/$REPOSITORY_NAME/$groupToUrlPath/${ac.artifactId}-$presetName/${ac.version}/$artFileName")
                    .apply { println("URL is $this") }
            val file = dir.resolve(artFileName).apply { createNewFile() }
            file.writeBytes(url.openStream().readBytes())
            unzip(file, dir)
        }

        val extractedDir = dir.resolve("$presetName-${ac.version}")
        if (ac.agentPath == null)
            ac.agentPath = extractedDir.listFiles()?.first { file ->
                dynamicLibExtensions.any { it == file.extension }
            }
        if (ac.runtimePath == null)
            ac.runtimePath = extractedDir
        println("!!!! ${ac.toJvmArgs()}")
    }

    private fun unzip(file: File, dir: File) {
        ZipFile(file).use { zip ->
            zip.entries().asSequence().forEach { entry ->
                if (entry.isDirectory) File(dir, entry.name).mkdirs()
                else {
                    zip.getInputStream(entry).use { input ->
                        File(dir, entry.name).outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                }
            }
        }
    }
}