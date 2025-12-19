package com.github.seepick.kaml.k8s.deployment

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.k8s.K8s
import com.github.seepick.kaml.kerror

fun K8s.deployment(code: DeploymentDsl.() -> Unit): K8sDeployment =
    DeploymentDsl().apply(code).build()

@KamlDsl
class DeploymentDsl {
    /** The visible label of the deployment.*/
    var name = "default-deployment-name"
    /** Number of pods to run simultaneously. */
    var replicas = 1
    /** ? */
    var selectorMatchLabelsApp = "default-app"
    private var templateInstance: Template? = null

    /** How a pod should look like when being created. */
    fun template(code: TemplateDsl.() -> Unit) {
        templateInstance = TemplateDsl().apply(code).build()
    }

    internal fun build() = K8sDeployment(
        metadata = DeploymentMetadata(name),
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

@KamlDsl
class ContainerDsl {

    /** Visible label for the container. */
    var name = "container-default-name"

    /** Container image, provided by a registry. */
    var image: Image? = null

    internal fun build() = Container(
        image = image ?: kerror("container image not set for [$name]"),
        name = name,
    )
}
