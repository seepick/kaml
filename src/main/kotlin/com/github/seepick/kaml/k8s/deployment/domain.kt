package com.github.seepick.kaml.k8s.deployment

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.KamlYamlOutput
import com.github.seepick.kaml.k8s.K8sArtifact
import com.github.seepick.kaml.k8s.ManifestKind

data class K8sDeployment(
    override val apiVersion: String = "apps/v1",
    override val metadata: DeploymentMetadata,
    override val spec: DeploymentSpec,
) : K8sArtifact<DeploymentMetadata, DeploymentSpec>, KamlYamlOutput {

    override val kind: ManifestKind = ManifestKind.Deployment

    override fun toYaml() = toYamlString()
}

data class DeploymentMetadata(
    val name: String,
)

data class DeploymentSpec(
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
