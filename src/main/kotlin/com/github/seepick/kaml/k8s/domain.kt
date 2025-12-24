package com.github.seepick.kaml.k8s

import com.github.seepick.kaml.Image

interface ApiVersion {
    val yamlValue: String
}

enum class K8sApiVersion(override val yamlValue: String) : ApiVersion {
    Pod("v1"),
    Service("v1"),
    Deployment("apps/v1"),
    ReplicaSet("apps/v1"),
}

//data class CustomApiVersion(val group...name: String) : ApiVersion

interface Manifest<Spec> {
    val apiVersion: ApiVersion
    val kind: ManifestKind
    val metadata: Metadata
    val spec: Spec
}

data class Metadata(
    val name: String?,
    val labels: Map<String, String>,
) {
    companion object {
        val default = Metadata(name = null, labels = emptyMap())
    }
}

enum class ManifestKind(val yamlValue: String) {
    Deployment("Deployment"),
    Pod("Pod"),
    Service("Service"),
    // ReplicaSet
}

data class Container(
    val image: Image,
    val name: String,
    val ports: List<Port>,
    val env: Map<String, Any>,
)

data class Port(
    val name: String?,
    val containerPort: Int?,
    val protocol: Protocol?,
    val port: Int?,
    val targetPort: Int?,
    val nodePort: Int?, // 30080
)

enum class Protocol(val yamlValue: String) {
    TCP("TCP"), UDP("UDP")
}
