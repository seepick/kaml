package com.github.seepick.kaml.k8s

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.yaml.YamlMapDsl

data class Metadata(
    val name: String?,
    val labels: Map<String, String>,
) {
    companion object {
        val default = Metadata(name = null, labels = emptyMap())
    }
}

@KamlDsl
class MetadataDsl {

    /** The visible label of this artifact.*/
    var name: String? = null

    /** Arbitrary key-value information; for grouping/selecting. */
    val labels = mutableMapOf<String, String>()

    fun build() = Metadata(
        name = name,
        labels = labels,
    )
}

fun YamlMapDsl.addMetadata(metadata: Metadata) {
    map("metadata") {
        if (metadata.name != null) add("name", metadata.name)
        if (metadata.labels.isNotEmpty()) {
            map("labels") {
                addAll(metadata.labels)
            }
        }
    }
}
