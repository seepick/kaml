package com.github.seepick.kaml.k8s.artifacts.networkPolicy

import com.github.seepick.kaml.KamlYamlOutput
import com.github.seepick.kaml.k8s.shared.K8sApiVersion
import com.github.seepick.kaml.k8s.shared.Manifest
import com.github.seepick.kaml.k8s.shared.ManifestKind
import com.github.seepick.kaml.k8s.shared.Metadata
import com.github.seepick.kaml.k8s.shared.Selector
import com.github.seepick.kaml.k8s.shared.SimplePort
import com.github.seepick.kaml.validation.Validatable
import com.github.seepick.kaml.validation.validation

data class NetworkPolicy(
    override val metadata: Metadata,
    override val spec: NetworkPolicySpec
) : Manifest<NetworkPolicySpec>, KamlYamlOutput, Validatable {

    override val apiVersion = K8sApiVersion.NetworkingV1
    override val kind = ManifestKind("NetworkPolicy")

    override fun validate() = validation {}
    override fun toYamlNode() = _toYamlNode()
}

enum class PolicyType(val yamlValue: String) {
    Ingress("Ingress"),
    Egress("Egress")
}

data class NetworkPolicySpec(
    val podSelector: Selector,
    // policyTypes ... inferred ;)
    val ingresses: List<Ingress>,
)

data class Ingress(
    val ports: List<SimplePort>,
    val fromRules: List<Rule>,

    ) : Validatable {
    override fun validate() = validation {
        valid(!ports.isEmpty()) { "At least one port most be defined for ingress!" }
        valid(fromRules.isNotEmpty()) {
            "At least one rule most be defined for ingress!"
        }
    }
}
