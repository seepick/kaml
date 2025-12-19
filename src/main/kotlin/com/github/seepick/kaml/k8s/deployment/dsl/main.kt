package com.github.seepick.kaml.k8s.deployment.dsl

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.k8s.K8s
import com.github.seepick.kaml.k8s.deployment.domain.Container
import com.github.seepick.kaml.k8s.deployment.domain.K8sDeployment
import com.github.seepick.kaml.k8s.deployment.domain.Selector
import com.github.seepick.kaml.k8s.deployment.domain.Template

class ContainerDsl {
    var name = "container-default-name"
    var image: Image? = null

    fun build() = Container(
        image = image ?: error("container image not set for [$name]"),
        name = name,
    )
}

class TemplateDsl {

    var metadataLabelsApp = ""
    private val containerList = mutableListOf<Container>()

    fun container(code: ContainerDsl.() -> Unit) {
        containerList += ContainerDsl().apply(code).build()
    }

    internal fun build() = Template(
        metadataLabelsApp = metadataLabelsApp,
        containers = containerList,
    )
}

class DeploymentDsl {
    var name = "default-deployment-name"
    var replicas = 1
    var selectorMatchLabelsApp = "default-app"
    private var templateInstance: Template? = null

    fun template(code: TemplateDsl.() -> Unit) {
        templateInstance = TemplateDsl().apply(code).build()
    }

    internal fun build() = K8sDeployment(
        name = name,
        replicas = replicas,
        selector = Selector(matchLabelsApp = selectorMatchLabelsApp),
        template = templateInstance ?: error("deployment template not set for [$name]"),
    )
}

fun K8s.deployment(code: DeploymentDsl.() -> Unit): K8sDeployment =
    DeploymentDsl().apply(code).build()
