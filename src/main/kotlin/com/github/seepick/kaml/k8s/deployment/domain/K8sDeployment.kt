package com.github.seepick.kaml.k8s.deployment.domain

import com.github.seepick.kaml.GenericImage
import com.github.seepick.kaml.Image

data class K8sDeployment(
    val name: String,
    val replicas: Int,
    val selector: Selector,
    val template: Template,
)

data class Selector(
    val matchLabelsApp: String,
)

data class Template(
    val metadataLabelsApp: String,
    val containers: List<Container>,
)

data class Container(
    val image: Image,
    val name: String,
)
