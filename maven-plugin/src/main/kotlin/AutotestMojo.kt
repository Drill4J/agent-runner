@file:Suppress("unused")

import com.epam.drill.agent.runner.AgentConfiguration
import com.epam.drill.agent.runner.Configuration
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.plugins.annotations.ResolutionScope

@Mojo(
    name = "autotest",
    defaultPhase = LifecyclePhase.INITIALIZE, requiresDependencyResolution = ResolutionScope.RUNTIME, threadSafe = true
)
class AutotestMojo : AgentMojo() {

    @Parameter(required = true, property = "drill")
    lateinit var drill: AgentConfiguration

    override val config: Configuration
        get() = drill


}