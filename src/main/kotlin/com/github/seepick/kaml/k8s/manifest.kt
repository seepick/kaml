package com.github.seepick.kaml.k8s

import com.github.seepick.kaml.yaml.YamlMapDsl


interface Manifest<Spec> {
    val apiVersion: ApiVersion
    val kind: ManifestKind
    val metadata: Metadata
    val spec: Spec
}

enum class ManifestKind(val yamlValue: String) {
    Deployment("Deployment"),
    Pod("Pod"),
    Service("Service"),
    // ReplicaSet
}

fun <Spec> YamlMapDsl.addManifest(manifest: Manifest<Spec>, spec: YamlMapDsl.() -> Unit) {
    add("apiVersion", manifest.apiVersion.yamlValue)
    add("kind", manifest.kind.yamlValue)
    addMetadata(manifest.metadata)
    map("spec") {
        spec()
    }
}
