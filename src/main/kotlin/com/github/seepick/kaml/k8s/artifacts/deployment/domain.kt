package com.github.seepick.kaml.k8s.artifacts.deployment

import com.github.seepick.kaml.KamlYamlOutput
import com.github.seepick.kaml.Validatable
import com.github.seepick.kaml.buildValidationResult
import com.github.seepick.kaml.k8s.shared.Container
import com.github.seepick.kaml.k8s.shared.K8sApiVersion
import com.github.seepick.kaml.k8s.shared.Manifest
import com.github.seepick.kaml.k8s.shared.ManifestKind
import com.github.seepick.kaml.k8s.shared.Metadata
import com.github.seepick.kaml.k8s.shared.addContainers
import com.github.seepick.kaml.k8s.shared.addMetadata
import com.github.seepick.kaml.yaml.YamlRoot

data class Deployment(
    override val metadata: Metadata,
    override val spec: DeploymentSpec,
) : Manifest<DeploymentSpec>, KamlYamlOutput {

    override val apiVersion = K8sApiVersion.Deployment

    override val kind: ManifestKind = ManifestKind.Deployment

    override fun toYamlNode() = YamlRoot.k8sManifest(this) {
        add("replicas", spec.replicas)
        map("selector") {
            map("matchLabels") {
                addAll(spec.selector.matchLabels)
            }
        }
        map("template") {
            addMetadata(spec.template.metadata)
            map("spec") {
                addContainers(spec.template.containers)
            }
        }
    }
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
) : Validatable {
    companion object {
        val default = Template(Metadata.default, containers = emptyList())
    }

    override fun validate() = buildValidationResult {
        check({ !containers.isEmpty() }, "Template must contain at least one container ($metadata)")
        containers.forEach { mergeWith(it) }
    }
}
