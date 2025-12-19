package com.github.seepick.kaml.k8s.deployment

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.k8s.ContainerDsl
import com.github.seepick.kaml.k8s.GeneralMetadata
import com.github.seepick.kaml.k8s.K8s
import com.github.seepick.kaml.kerror

fun K8s.deployment(code: DeploymentDsl.() -> Unit): Deployment =
    DeploymentDsl().apply(code).build()

@KamlDsl
class DeploymentDsl {
    /** The visible label of the deployment.*/
    var name = "default-deployment-name"

    var labels = emptyMap<String, String>()

    /** Number of pods to run simultaneously. */
    var replicas = 1
    /** ? */
    var selectorMatchLabelsApp = "default-app"
    private var templateInstance: Template? = null

    /** How a pod should look like when being created. */
    fun template(code: TemplateDsl.() -> Unit) {
        templateInstance = TemplateDsl().apply(code).build()
    }

    internal fun build() = Deployment(
        metadata = GeneralMetadata(name, labels),
        spec = DeploymentSpec(
            replicas = replicas,
            selector = Selector(matchLabelsApp = selectorMatchLabelsApp),
            template = templateInstance ?: kerror("deployment template not set for [$name]")
        ),
    )
}

@KamlDsl
class TemplateDsl {

    /** ? */
    var metadataLabelsApp = ""

    private val containerList = mutableListOf<Container>()

    /** A list of pods running in a single container. */
    fun container(code: ContainerDsl.() -> Unit) {
        containerList += ContainerDsl().apply(code).build()
    }

    internal fun build() = Template(
        metadataLabelsApp = metadataLabelsApp,
        containers = containerList,
    )
}
