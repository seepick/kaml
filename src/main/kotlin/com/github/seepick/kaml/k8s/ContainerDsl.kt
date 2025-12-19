package com.github.seepick.kaml.k8s

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.k8s.deployment.Container
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
