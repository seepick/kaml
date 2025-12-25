package com.github.seepick.kaml.k8s.artifacts.pod

import com.github.seepick.kaml.KamlYamlOutput
import com.github.seepick.kaml.k8s.shared.ApiVersion
import com.github.seepick.kaml.k8s.shared.Container
import com.github.seepick.kaml.k8s.shared.Manifest
import com.github.seepick.kaml.k8s.shared.ManifestKind
import com.github.seepick.kaml.k8s.shared.Metadata
import com.github.seepick.kaml.k8s.shared.addContainers
import com.github.seepick.kaml.yaml.YamlRoot

data class Pod(
    override val apiVersion: ApiVersion,
    override val metadata: Metadata,
    override val spec: PodSpec,
    // status = {}
) : Manifest<PodSpec>, KamlYamlOutput {

    override val kind: ManifestKind = ManifestKind.Pod

    override fun toYamlNode() = YamlRoot.k8sManifest(this) {
        addContainers(spec.containers)
    }
}

data class PodSpec(
    val containers: List<Container>,
    // dnsPolicy = ClusterFirst, ...
    // restartPolicy = Always, ...
)
