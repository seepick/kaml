package com.github.seepick.kaml.k8s.deployment

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.KamlYamlOutput
import com.github.seepick.kaml.k8s.GeneralMetadata
import com.github.seepick.kaml.k8s.Manifest
import com.github.seepick.kaml.k8s.ManifestKind

data class Deployment(
    override val apiVersion: String = "apps/v1",
    override val metadata: GeneralMetadata,
    override val spec: DeploymentSpec,
) : Manifest<DeploymentSpec>, KamlYamlOutput {

    override val kind: ManifestKind = ManifestKind.Deployment

    override fun toYaml() = toYamlString()
}


data class DeploymentSpec(
    val replicas: Int,
    val selector: Selector,
    val template: Template,
)

data class Selector(
    val matchLabelsApp: String,
)

data class Template(
    // FIXME refactor to Metadata.labels.Map<String, String>
    val metadataLabelsApp: String,
    val containers: List<Container>,
)

data class Container(
    val image: Image,
    val name: String,
)
