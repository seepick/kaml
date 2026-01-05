package com.github.seepick.kaml.k8s.kustomize

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.k8s.artifacts.deployment.Deployment

data class Overlay(
    val name: OverlayName,
    val modifiers: List<Modifier>,
)

data class Modifier(
    val deployment: Deployment,
    val operation: (Deployment) -> Deployment,
)

@KamlDsl
class OverlayDsl(val name: OverlayName) {

    private val modifiers = mutableListOf<Modifier>()
    fun change(deployment: Deployment, modifier: (Deployment) -> Deployment) {
        modifiers += Modifier(deployment, modifier)
    }

    fun build(): Overlay = Overlay(
        name = name,
        modifiers = modifiers,
    )
}
