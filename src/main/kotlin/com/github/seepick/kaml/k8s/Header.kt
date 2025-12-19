package com.github.seepick.kaml.k8s

import com.amihaiemil.eoyaml.YamlNode
import com.github.seepick.kaml.fixDashPlacement
import com.github.seepick.kaml.yamlMap

interface K8sArtifact<MetaData, Spec> {
    val apiVersion: String
    val kind: ManifestKind
    val metadata: MetaData
    val spec: Spec
}

enum class ManifestKind(val yamlValue: String) {
    Deployment("Deployment")
}

fun <MetaData, Spec> K8sArtifact<MetaData, Spec>.toYamlPattern(
    buildMetadata: (MetaData) -> YamlNode,
    buildSpec: (Spec) -> YamlNode,
): String {

    val root = yamlMap()
    root.add("apiVersion", apiVersion)
    root.add("kind", kind.yamlValue)
    root.add("metadata", buildMetadata(metadata))
    root.add("spec", buildSpec(spec))
    // maybe more specifics...?
    return root.build().toString()
        .let(::fixDashPlacement) // steps mapping-sequence with "-" prefixed
}
