package com.github.seepick.kaml.k8s

import com.github.seepick.kaml.Image

interface Manifest<Spec> {
    //    Pod/Service: apiVersion="v1"
//    ReplicaSet/Deployment: apiVersion="apps/v1"
    val apiVersion: String
    val kind: ManifestKind
    val metadata: Metadata
    val spec: Spec
}

data class Metadata(
    val name: String,
    val labels: Map<String, String>,
) {
    companion object {
        val default = Metadata(name = "default-name", labels = emptyMap())
    }
}

enum class ManifestKind(val yamlValue: String) {
    Deployment("Deployment"),
    Pod("Pod"),
    // Service
    // Deployment
    // ReplicaSet
}

data class Container(
    val image: Image,
    val name: String,
    val ports: List<Port>,
)

data class Port(
    val name: String,
    val containerPort: Int,
)
