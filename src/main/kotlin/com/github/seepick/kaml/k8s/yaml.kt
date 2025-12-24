package com.github.seepick.kaml.k8s

import com.amihaiemil.eoyaml.YamlNode
import com.github.seepick.kaml.yaml.addKeyValues
import com.github.seepick.kaml.yaml.toCleanYamlString
import com.github.seepick.kaml.yaml.yamlMap

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
    return root.build().toCleanYamlString()
}

