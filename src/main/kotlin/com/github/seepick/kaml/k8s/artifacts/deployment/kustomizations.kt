package com.github.seepick.kaml.k8s.artifacts.deployment

import com.github.seepick.kaml.GenericImage
import com.github.seepick.kaml.Image
import com.github.seepick.kaml.k8s.artifacts.pod.containers
import com.github.seepick.kaml.k8s.shared.Container

val Deployment.kustomize get() = DeploymentKustomizer(this)

class DeploymentKustomizer(private val deployment: Deployment) {

    private val containersLens = Deployment.spec.template.spec.containers

    fun image(
        containerMatcher: Container.() -> Boolean,
        imageProvider: Container.() -> Image,
    ): Deployment {
        val oldContainers = containersLens.get(deployment)
        val oldContainer = oldContainers.firstOrNull(containerMatcher) ?: error("No container matched!")
        return containersLens.set(deployment, buildList {
            addAll(oldContainers)
            if (!remove(oldContainer)) error("Failed to remove: $oldContainer!")
            val newContainer = oldContainer.copy(image = imageProvider(oldContainer))
            if (!add(newContainer)) error("Failed to add: $newContainer!")
        })
    }

    fun imageTag(imageName: String, newImageTagVersion: String) = image(
        containerMatcher = { image.name == imageName },
        imageProvider = { (image as GenericImage).copy(version = newImageTagVersion) }
    )
}
