package com.github.seepick.kaml.k8s.shared

interface ApiVersion {
    val yamlValue: String
}

enum class K8sApiVersion(override val yamlValue: String) : ApiVersion {
    v1("v1"),
    NetworkingV1("networking.k8s.io/v1"),
    AppsV1("apps/v1"), // ReplicaSet
}

//data class CustomApiVersion(val group...name: String) : ApiVersion

enum class Protocol(val yamlValue: String) {
    TCP("TCP"),
    UDP("UDP"),
    SCTP("SCTP");
}
