package com.github.seepick.kaml.k8s.shared

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.yaml.YamlMapDsl

data class Metadata(
    val name: String?,
    val namespace: String?,
    val labels: Map<String, String>,
) {
    companion object {
        val default = Metadata(name = null, namespace = null, labels = emptyMap())
    }
}

@KamlDsl
class MetadataDsl {

    /** The visible label of this artifact.*/
    var name: String? = null

    var namespace: String? = null

    /** Arbitrary key-value information; for grouping/selecting. */
    val labels = mutableMapOf<String, String>() // TODO could create Label type

    fun build() = Metadata(
        name = name,
        labels = labels,
        namespace = namespace,
    )
}

fun YamlMapDsl.addMetadata(metadata: Metadata) {
    map("metadata") {
        addIfNotNull("name", metadata.name)
        addIfNotNull("namespace", metadata.namespace)
        if (metadata.labels.isNotEmpty()) {
            map("labels") {
                addAll(metadata.labels)
            }
        }
    }
}
