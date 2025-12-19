package com.github.seepick.kaml.k8s

interface Manifest<Spec> {
    //    Pod/Service: apiVersion="v1"
//    ReplicaSet/Deployment: apiVersion="apps/v1"
    val apiVersion: String
    val kind: ManifestKind
    val metadata: GeneralMetadata
    val spec: Spec
}

data class GeneralMetadata(
    val name: String,
    val labels: Map<String, String>,
)

enum class ManifestKind(val yamlValue: String) {
    Deployment("Deployment"),
    Pod("Pod"),
    // Service
    // Deployment
    // ReplicaSet
}
