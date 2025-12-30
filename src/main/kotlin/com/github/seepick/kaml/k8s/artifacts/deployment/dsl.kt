package com.github.seepick.kaml.k8s.artifacts.deployment

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.KamlKonfig
import com.github.seepick.kaml.k8s.K8s
import com.github.seepick.kaml.k8s.XK8s
import com.github.seepick.kaml.k8s.artifacts.pod.PodOrTemplateDsl
import com.github.seepick.kaml.k8s.shared.Metadata
import com.github.seepick.kaml.k8s.shared.MetadataDsl
import com.github.seepick.kaml.validation.handleValidation

fun K8s.deployment(konfig: KamlKonfig = KamlKonfig.default, code: DeploymentDsl.() -> Unit): Deployment =
    DeploymentDsl(konfig).apply(code).build()

fun XK8s.deployment(code: DeploymentDsl.() -> Unit): Deployment =
    K8s.deployment(konfig, code)

@KamlDsl
class DeploymentDsl(private val konfig: KamlKonfig) {

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

    internal fun build() = handleValidation(
        konfig, Deployment(
            metadata = metadata,
            spec = DeploymentSpec(
                replicas = replicas,
                selector = selector,
                template = template,
            ),
        )
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
class TemplateDsl : PodOrTemplateDsl<Template>() {
    override fun build() = Template(
        metadata = _metadata,
        containers = containers,
    )
}
