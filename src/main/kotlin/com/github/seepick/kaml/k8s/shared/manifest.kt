package com.github.seepick.kaml.k8s.shared

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
    ConfigMap("ConfigMap"),
    Service("Service"),
    Namespace("Namespace"),
    ResourceQuota("ResourceQuota"),
    Secret("Secret")
    // ReplicaSet
}

fun <Spec> YamlMapDsl.addManifest(manifest: Manifest<Spec>, skipSpec: Boolean, spec: YamlMapDsl.() -> Unit) {
    add("apiVersion", manifest.apiVersion.yamlValue)
    add("kind", manifest.kind.yamlValue)
    addMetadata(manifest.metadata)
    if (!skipSpec) {
        map("spec") {
            spec()
        }
    }
}
