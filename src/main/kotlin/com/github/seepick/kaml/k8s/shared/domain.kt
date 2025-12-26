package com.github.seepick.kaml.k8s.shared

interface ApiVersion {
    val yamlValue: String
}

enum class K8sApiVersion(override val yamlValue: String) : ApiVersion {
    Pod("v1"),
    Service("v1"),
    Namespace("v1"),
    ResourceQuota("v1"),
    Deployment("apps/v1"),
    ReplicaSet("apps/v1"),
}

//data class CustomApiVersion(val group...name: String) : ApiVersion

enum class Protocol(val yamlValue: String) {
    TCP("TCP"), UDP("UDP"), SCTP("SCTP")
}
