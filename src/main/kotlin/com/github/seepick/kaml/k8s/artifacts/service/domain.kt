package com.github.seepick.kaml.k8s.artifacts.service

import com.github.seepick.kaml.KamlYamlOutput
import com.github.seepick.kaml.k8s.shared.K8sApiVersion
import com.github.seepick.kaml.k8s.shared.Manifest
import com.github.seepick.kaml.k8s.shared.ManifestKind
import com.github.seepick.kaml.k8s.shared.Metadata
import com.github.seepick.kaml.k8s.shared.ServicePort
import com.github.seepick.kaml.k8s.shared.addServicePorts
import com.github.seepick.kaml.yaml.YamlRoot

data class Service(
    override val metadata: Metadata,
    override val spec: ServiceSpec,
) : Manifest<ServiceSpec>, KamlYamlOutput {
    override val apiVersion = K8sApiVersion.Service
    override val kind = ManifestKind.Service
    override fun toYamlNode() = YamlRoot.k8sManifest(this) {
        add("type", spec.type.yamlValue)
        addServicePorts(spec.ports)
        map("selector") {
            addAll(spec.selector)
        }
    }
}

data class ServiceSpec(
    val type: ServiceType,
    val ports: List<ServicePort>,
    val selector: Map<String, String>,
)

enum class ServiceType(val yamlValue: String) {
    /** group pods together, providing single access interface */
    ClusterIP("ClusterIP"),
    /** enable access from outside the cluster */
    NodePort("NodePort"),
    LoadBalancer("LoadBalancer");

    companion object {
        val default = ClusterIP
    }
}
