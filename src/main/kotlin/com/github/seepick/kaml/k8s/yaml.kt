package com.github.seepick.kaml.k8s

import com.amihaiemil.eoyaml.YamlNode
import com.github.seepick.kaml.addKeyValues
import com.github.seepick.kaml.fixDashPlacement
import com.github.seepick.kaml.yamlMap

internal fun <Spec> Manifest<Spec>.toYamlPattern(
//    buildMetadata: (MetaData) -> YamlNode,
    buildSpec: (Spec) -> YamlNode,
): String {

    val root = yamlMap()
    root.add("apiVersion", apiVersion)
    root.add("kind", kind.yamlValue)

    root.add(
        "metadata", yamlMap()
        .add("name", metadata.name)
        .also {
            if (this.metadata.labels.isNotEmpty()) {
                it.add("labels", yamlMap().addKeyValues(metadata.labels).build())
            }
        }
        .build())
    root.add("spec", buildSpec(spec))
    // maybe more specifics...?
    return root.build().toString()
        .let(::fixDashPlacement) // steps mapping-sequence with "-" prefixed
}
