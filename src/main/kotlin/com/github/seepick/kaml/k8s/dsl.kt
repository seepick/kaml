package com.github.seepick.kaml.k8s

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.kerror

@KamlDsl
class ContainerDsl {

    /** Visible label for the container. */
    var name = "container-default-name"

    /** Container image, provided by a registry. */
    var image: Image? = null

    internal fun build() = Container(
        image = image ?: kerror("container image not set for [$name]"),
        name = name,
    )
}

@KamlDsl
class MetadataDsl {

    /** The visible label of this artifact.*/
    var name: String = "default-name"

    /** Arbitrary key-value information; for grouping/selecting. */
    val labels = mutableMapOf<String, String>()

    fun build() = Metadata(
        name = name,
        labels = labels,
    )
}
