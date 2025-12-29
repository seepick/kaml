package com.github.seepick.kaml.k8s.shared

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.ImageFormatter
import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.Validatable
import com.github.seepick.kaml.kerror
import com.github.seepick.kaml.validation
import com.github.seepick.kaml.yaml.YamlMapDsl

data class Container(
    val image: Image,
    /** Mandatory by k8s */
    val name: String,
    // resources = {}
    val ports: List<ContainerPort>,
    val env: Env,
    val resources: Resources?,
) : Validatable {
    override fun validate() = validation {
        check(name.isNotEmpty(), "Container name must not be empty")
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

    private var resources: Resources? = null
    fun resources(code: ResourcesDsl.() -> Unit) {
        resources = ResourcesDsl().apply(code).build()
    }

    internal fun build() = Container(
        image = image ?: kerror("container image not set for [$name]"),
        name = name,
        ports = ports,
        env = env,
        resources = resources,
    )
}


fun YamlMapDsl.addContainers(containers: List<Container>) {
    seq("containers") {
        containers.forEach { container ->
            flatMap {
                add("name", container.name)
                add("image", container.image.format(ImageFormatter.Docker))
                addContainerPorts(container.ports)
                addEnv(container.env)
                addResources(container.resources)
            }
        }
    }
}
