package com.github.seepick.kaml.k8s.artifacts.pod

import arrow.optics.optics
import com.github.seepick.kaml.KamlYamlOutput
import com.github.seepick.kaml.k8s.shared.ApiVersion
import com.github.seepick.kaml.k8s.shared.Container
import com.github.seepick.kaml.k8s.shared.HostPath
import com.github.seepick.kaml.k8s.shared.Manifest
import com.github.seepick.kaml.k8s.shared.ManifestKind
import com.github.seepick.kaml.k8s.shared.Metadata
import com.github.seepick.kaml.k8s.shared.Volume
import com.github.seepick.kaml.validation.Validatable
import com.github.seepick.kaml.validation.ValidationResult
import com.github.seepick.kaml.validation.validation

/** See https://kubernetes.io/docs/concepts/workloads/pods/ */
data class Pod(
    override val apiVersion: ApiVersion,
    override val metadata: Metadata,
    override val spec: PodSpec,
    // status = {}
) : Manifest<PodSpec>, KamlYamlOutput, Validatable {

    override val kind: ManifestKind = ManifestKind("Pod")

    override fun toYamlNode() = _toYamlNode()

    override fun validate() = ValidationResult.Valid
}

@optics
data class PodSpec(
    val containers: List<Container>,
    // dnsPolicy = ClusterFirst, ...
    val restartPolicy: RestartPolicy?,
    val volumes: List<Volume>,
    val hostPath: HostPath?,
) : Validatable {
    override fun validate() = validation {
        valid(!containers.isEmpty()) { "Pod spec must contain at least one container!" }
    }

    companion object {} // for optics
}

enum class RestartPolicy(val yamlValue: String) {
    Always("Always"),
    Never("Never"),
    OnFailure("OnFailure");

    companion object {
        val default = Always
    }
}
