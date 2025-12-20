package com.github.seepick.kaml.k8s.pod

import com.github.seepick.kaml.KamlYamlOutput
import com.github.seepick.kaml.k8s.Container
import com.github.seepick.kaml.k8s.Manifest
import com.github.seepick.kaml.k8s.ManifestKind
import com.github.seepick.kaml.k8s.Metadata

data class Pod(
    override val apiVersion: String,
    override val metadata: Metadata,
    override val spec: PodSpec,
) : Manifest<PodSpec>, KamlYamlOutput {

    override val kind: ManifestKind = ManifestKind.Pod

    override fun toYaml() = toYamlString()
}

data class PodSpec(
    val containers: List<Container>,
)
