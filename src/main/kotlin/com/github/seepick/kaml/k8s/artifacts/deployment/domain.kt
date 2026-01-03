package com.github.seepick.kaml.k8s.artifacts.deployment

import arrow.optics.optics
import com.github.seepick.kaml.KamlYamlOutput
import com.github.seepick.kaml.k8s.artifacts.pod.PodSpec
import com.github.seepick.kaml.k8s.artifacts.pod.addPodSpec
import com.github.seepick.kaml.k8s.shared.K8sApiVersion
import com.github.seepick.kaml.k8s.shared.Manifest
import com.github.seepick.kaml.k8s.shared.ManifestKind
import com.github.seepick.kaml.k8s.shared.Metadata
import com.github.seepick.kaml.k8s.shared.Selector
import com.github.seepick.kaml.k8s.shared.addMetadata
import com.github.seepick.kaml.validation.Validatable
import com.github.seepick.kaml.validation.ValidationResult
import com.github.seepick.kaml.validation.validation
import com.github.seepick.kaml.yaml.YamlRoot

/** See https://kubernetes.io/docs/concepts/workloads/controllers/deployment/ */
@optics
data class Deployment(
    override val metadata: Metadata,
    override val spec: DeploymentSpec,
) : Manifest<DeploymentSpec>, KamlYamlOutput, Validatable {

    companion object {} // for optics

    override val apiVersion = K8sApiVersion.AppsV1

    override val kind: ManifestKind = ManifestKind("Deployment")

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
                addPodSpec(spec.template.spec)
            }
        }
    }

    override fun validate() = ValidationResult.Valid
}

@optics
data class DeploymentSpec(
    val replicas: Int,
    val selector: Selector,
    val template: Template,
    // strategy: Recreate | RollingUpdate
    // if RollingUpdate: rollingUpdate info expected (otherwise fail); maxSurge, maxUnavailable (as Int and percentage)
) {
    companion object {} // for optics
}

@optics
data class Template(
    val metadata: Metadata,
    val spec: PodSpec,
) : Validatable {
    override fun validate() = validation {
    }

    companion object {} // for optics
}
