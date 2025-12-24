package com.github.seepick.kaml.k8s

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.kerror

@KamlDsl
class ContainerDsl {

    /** Visible label for the container. */
    var name = "container-default-name"

    /** Container image, provided by a registry. */
    var image: Image? = null

    private val ports = mutableListOf<Port>()

    fun ports(code: PortDsl.() -> Unit) {
        ports += PortDsl().apply(code).build()
    }

    internal fun build() = Container(
        image = image ?: kerror("container image not set for [$name]"),
        name = name,
        ports = ports,
    )
}

@KamlDsl
class PortDsl {

    var name: String = "default-port-name"
    var containerPort: Int = 8080

    internal fun build() = Port(
        name = name,
        containerPort = containerPort,
    )
}

@KamlDsl
class MetadataDsl {

    /** The visible label of this artifact.*/
    var name: String = "default-name"

    /** Arbitrary key-value information; for grouping/selecting. */
    val labels = mutableMapOf<String, String>()

    fun build() = Metadata(
        name = name,
        labels = labels,
    )
}

abstract class PodOrTemplateDsl<POT>() {

    private var _metadata: Metadata? = null
    protected val metadata get() = _metadata ?: kerror("metadata not set")

    fun metadata(code: MetadataDsl.() -> Unit) {
//      TODO ?  require(metadata == null) { "deployment template metadata already set" }
        _metadata = MetadataDsl().also { it.name = "default-pod-name" }.apply(code).build()
    }

    protected val containers = mutableListOf<Container>()
    /** A list of pods running in a single container. */
    fun container(code: ContainerDsl.() -> Unit) {
        containers += ContainerDsl().apply(code).build()
    }

    internal abstract fun build(): POT
}
