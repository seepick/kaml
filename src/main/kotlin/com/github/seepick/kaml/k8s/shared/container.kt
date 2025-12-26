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
    val env: Map<String, Any>,
) : Validatable {
    override fun validate() = validation {
        check(name.isNotEmpty(), "Container name must not be empty")
    }
}

@KamlDsl
class ContainerDsl {
    /** Visible label for the container. */
    var name: String = ""
    /** Container image, provided by a registry. */
    var image: Image? = null
    val env = mutableMapOf<String, Any>()

    private val ports = mutableListOf<ContainerPort>()
    fun ports(code: ContainerPortDsl.() -> Unit) {
        ports += ContainerPortDsl().apply(code).build()
    }

    internal fun build() = Container(
        image = image ?: kerror("container image not set for [$name]"),
        name = name,
        ports = ports,
        env = env,
    )
}

fun YamlMapDsl.addContainers(containers: List<Container>) {
    seq("containers") {
        containers.forEach { container ->
            flatMap {
                addIfNotNull("name", container.name)
                add("image", container.image.format(ImageFormatter.Docker))
                addContainerPorts(container.ports)
                addEnv(container.env)
            }
        }
    }
}
