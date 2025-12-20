package com.github.seepick.kaml.k8s.deployment

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.k8s.Container
import com.github.seepick.kaml.k8s.ContainerDsl
import com.github.seepick.kaml.k8s.K8s
import com.github.seepick.kaml.k8s.Metadata
import com.github.seepick.kaml.k8s.MetadataDsl
import com.github.seepick.kaml.kerror

fun K8s.deployment(code: DeploymentDsl.() -> Unit): Deployment =
    DeploymentDsl().apply(code).build()

@KamlDsl
class DeploymentDsl {

    companion object {
        private const val DEFAULT_NAME = "default-deployment-name"
    }
    /** Number of pods to run simultaneously. */
    var replicas = 1

    /** How to filter for affected pods. */
    private var selector = Selector.default
    fun selector(code: SelectorDsl.() -> Unit) {
        selector = SelectorDsl().apply(code).build()
    }

    private var metadata = Metadata.default.copy(name = DEFAULT_NAME)

    fun metadata(code: MetadataDsl.() -> Unit) {
        metadata = MetadataDsl().also { it.name = DEFAULT_NAME }.apply(code).build()
    }

    private var template = Template.default
    /** How a pod should look like when being created. */
    fun template(code: TemplateDsl.() -> Unit) {
        template = TemplateDsl().apply(code).build()
    }

    internal fun build() = Deployment(
        metadata = metadata,
        spec = DeploymentSpec(
            replicas = replicas,
            selector = selector,
            template = template,
        ),
    )
}

@KamlDsl
class SelectorDsl {
    val matchLabels = mutableMapOf<String, String>()
    fun build() = Selector(
        matchLabels = matchLabels
    )
}

@KamlDsl
class TemplateDsl {

    private var metadata: Metadata? = null
    fun metadata(code: MetadataDsl.() -> Unit) {
//        require(metadata == null) { "deployment template metadata already set" }
        metadata = MetadataDsl().apply(code).build()
    }

    private val containers = mutableListOf<Container>()
    /** A list of pods running in a single container. */
    fun container(code: ContainerDsl.() -> Unit) {
        containers += ContainerDsl().apply(code).build()
    }

    internal fun build() = Template(
        metadata = metadata ?: kerror("deployment template metadata not set"),
        containers = containers,
    )
}
