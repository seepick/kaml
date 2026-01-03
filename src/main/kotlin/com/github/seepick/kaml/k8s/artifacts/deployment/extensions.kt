package com.github.seepick.kaml.k8s.artifacts.deployment

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.k8s.artifacts.pod.containers
import com.github.seepick.kaml.k8s.shared.Container

private val containersLens = Deployment.spec.template.spec.containers

fun Deployment.imageTransformer(
    containerMatcher: (Container) -> Boolean,
    imageProvider: (Container) -> Image,
): Deployment {
    val oldContainers = containersLens.get(this)
    val oldContainer = oldContainers.firstOrNull(containerMatcher) ?: error("No container matched!")
    return containersLens.set(this, buildList {
        addAll(oldContainers)
        if (!remove(oldContainer)) error("Failed to remove: $oldContainer!")
        val newContainer = oldContainer.copy(image = imageProvider(oldContainer))
        if (!add(newContainer)) error("Failed to add: $newContainer!")
    })
}
