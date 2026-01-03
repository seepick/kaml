package com.github.seepick.kaml.k8s.shared

import com.github.seepick.kaml.yaml.YamlMapDsl

interface HasMetadata {
    val metadata: Metadata
}

interface Manifest<Spec> : HasMetadata {
    val apiVersion: ApiVersion
    val kind: ManifestKind
    val spec: Spec
}

@JvmInline
value class ManifestKind(val yamlValue: String)

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
