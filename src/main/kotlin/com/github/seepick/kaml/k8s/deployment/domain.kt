package com.github.seepick.kaml.k8s.deployment

import com.github.seepick.kaml.KamlYamlOutput
import com.github.seepick.kaml.k8s.Container
import com.github.seepick.kaml.k8s.K8sApiVersion
import com.github.seepick.kaml.k8s.Manifest
import com.github.seepick.kaml.k8s.ManifestKind
import com.github.seepick.kaml.k8s.Metadata

data class Deployment(
    override val metadata: Metadata,
    override val spec: DeploymentSpec,
) : Manifest<DeploymentSpec>, KamlYamlOutput {

    override val apiVersion = K8sApiVersion.Deployment

    override val kind: ManifestKind = ManifestKind.Deployment

    override fun toYaml() = toYamlString()
}


data class DeploymentSpec(
    val replicas: Int,
    val selector: Selector,
    val template: Template,
)

data class Selector(
    val matchLabels: Map<String, String>,
) {
    companion object {
        val default = Selector(emptyMap())
    }
}

data class Template(
    val metadata: Metadata,
    val containers: List<Container>,
) {
    companion object {
        val default = Template(Metadata.default, containers = emptyList())
    }
}

