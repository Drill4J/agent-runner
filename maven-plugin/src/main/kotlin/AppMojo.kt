import com.epam.drill.agent.runner.AppAgentConfiguration
import com.epam.drill.agent.runner.Configuration
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.plugins.annotations.ResolutionScope

@Mojo(
    name = "app",
    defaultPhase = LifecyclePhase.INITIALIZE, requiresDependencyResolution = ResolutionScope.RUNTIME, threadSafe = true
)
class AppMojo : AgentMojo() {

    @Parameter(required = true, property = "drill")
    lateinit var drill: AppAgentConfiguration

    override val config: Configuration
        get() = drill


}