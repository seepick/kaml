package com.github.seepick.kaml.k8s.shared

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.ImageFormatter
import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.validation.Validatable
import com.github.seepick.kaml.validation.invalid
import com.github.seepick.kaml.validation.validation
import com.github.seepick.kaml.yaml.YamlMapDsl

data class Container(
    val image: Image,
    /** Mandatory by k8s */
    val name: String,
    // resources = {}
    val ports: List<ContainerPort>,
    val env: Env,
    val resources: Resources?,
    val readinessProbe: Probe?,
    val livnessProbe: Probe?,
) : Validatable {
    override fun validate() = validation {
        valid(name.isNotEmpty()) { "Container name must not be empty" }
    }
}

@KamlDsl
class EnvDsl {

    val values = mutableMapOf<String, String>()

    val configMaps = mutableListOf<String>()
    val secrets = mutableListOf<String>()

    fun build() = Env(
        values = values,
        configMapRefNames = configMaps,
        secretRefNames = secrets,
    )
}

data class Env(
    val values: Map<String, String>,
    val configMapRefNames: List<String>,
    // TODO single values ref
    val secretRefNames: List<String>,
) {
    companion object {
        val default = Env(emptyMap(), emptyList(), emptyList())
    }

    val hasAny = values.isNotEmpty() || configMapRefNames.isNotEmpty() || secretRefNames.isNotEmpty()
}

data class Probe(
    val path: String,
    val port: Int,
) : Validatable {
    override fun validate() = validation {
        valid(port > 0) { "Port must be positive" }
    }
}

class ProbeDsl {
    private var path = "/health"
    private var port = 8080

    fun httpGet(path: String, port: Int) {
        // httpHeaders (name/value)
        this.path = path
        this.port = port
    }
    // TODO more probes
    // fun tcpSocket(port: Int)
    // fun exec(command: List<String>)

    // var initialDelaySeconds: Int = 10
    // how often to check
    // var periodSeconds: Int = 10
    // var failureThreshold: Int = 3

    internal fun build() = Probe(path, port)
}

@KamlDsl
class ContainerDsl {
    /** Visible label for the container. */
    var name: String = ""
    /** Container image, provided by a registry. */
    var image: Image? = null

    private var env = Env.default

    private val ports = mutableListOf<ContainerPort>()
    fun ports(code: ContainerPortDsl.() -> Unit) {
        ports += ContainerPortDsl().apply(code).build()
    }

    fun env(code: EnvDsl.() -> Unit) {
        env = EnvDsl().apply(code).build()
    }

    private var readinessProbe: Probe? = null
    fun readinessProbe(code: ProbeDsl.() -> Unit) {
        readinessProbe = ProbeDsl().apply(code).build()
    }

    private var livnessProbe: Probe? = null
    fun livnessProbe(code: ProbeDsl.() -> Unit) {
        livnessProbe = ProbeDsl().apply(code).build()
    }


    private var resources: Resources? = null
    fun resources(code: ResourcesDsl.() -> Unit) {
        resources = ResourcesDsl().apply(code).build()
    }

    internal fun build() = Container(
        image = image ?: invalid("Image must be set for container $name"),
        name = name,
        ports = ports,
        env = env,
        resources = resources,
        readinessProbe = readinessProbe,
        livnessProbe = livnessProbe,
    )
}

fun YamlMapDsl.addContainers(containers: List<Container>) {
    seq("containers") {
        containers.forEach { container ->
            flatMap {
                add("name", container.name)
                add("image", container.image.format(ImageFormatter.Docker))
                addContainerPorts(container.ports)
                addProbeIfNotNull("readinessProbe", container.readinessProbe)
                addProbeIfNotNull("livenessProbe", container.livnessProbe)
                addEnv(container.env)
                addResources(container.resources)
            }
        }
    }
}


fun YamlMapDsl.addProbeIfNotNull(key: String, probe: Probe?) {
    if (probe == null) return
    map(key) {
        map("httpGet") {
            add("path", probe.path)
            add("port", probe.port)
        }
    }
}