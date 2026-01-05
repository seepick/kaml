package com.github.seepick.kaml.k8s.kustomize

import com.github.seepick.kaml.Kaml
import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.toYamls
import com.github.seepick.kaml.validation.invalid
import io.github.oshai.kotlinlogging.KotlinLogging.logger

fun Kaml.kustomize(code: KustomizeDsl.() -> Unit): KustomizeWorld =
    KustomizeDsl().apply(code).build()

data class KustomizeWorld(
    val base: Base,
    val overlays: List<Overlay>,
) {
    private val log = logger {}
    fun generate(overlayName: OverlayName): String {
        // FIXME finish me
        val overlay = overlays.firstOrNull { it.name == overlayName } ?: error("No overlay with name $overlayName")
        val deployments = base.deployments.toMutableList()
        overlay.modifiers.forEach { modifier ->
            // TODO actually require metadata.name to be non-null...?!!
            val oldDeployment = deployments.firstOrNull { it.metadata.name == modifier.deployment.metadata.name }
                ?: error("Deployment not found: ${modifier.deployment}")
            // TODO maintain list order (search index first, and replace it there)
            if (!deployments.remove(oldDeployment)) error("Failed to remove deployment: $oldDeployment")
            log.debug { "Applying $modifier to $oldDeployment" }
            deployments += modifier.operation(oldDeployment)
        }
        return deployments.toYamls()
    }
}

@KamlDsl
class KustomizeDsl {
    private var base: Base? = null
    fun base(code: BaseDsl.() -> Unit): Base =
        BaseDsl().apply(code).build().also { base = it }

    private val overlays = mutableListOf<Overlay>()
    fun overlay(name: OverlayName, code: OverlayDsl.() -> Unit): Overlay =
        OverlayDsl(name).apply(code).build().also { overlays += it }

    fun build() = KustomizeWorld(
        base = base ?: invalid("Base must be set"),
        overlays = overlays,
    )
}

@JvmInline
value class OverlayName(val value: String)