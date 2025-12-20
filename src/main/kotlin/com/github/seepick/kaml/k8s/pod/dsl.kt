package com.github.seepick.kaml.k8s.pod

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.k8s.Container
import com.github.seepick.kaml.k8s.ContainerDsl
import com.github.seepick.kaml.k8s.K8s
import com.github.seepick.kaml.k8s.Metadata
import com.github.seepick.kaml.k8s.MetadataDsl

fun K8s.pod(code: PodDsl.() -> Unit): Pod =
    PodDsl().apply(code).build()

@KamlDsl
class PodDsl {

    companion object {
        private const val DEFAULT_NAME = "default-pod-name"
    }

    /** Read-only. Defaults to "v1". */
    val apiVersion = "v1"

    private val containers = mutableListOf<Container>()

    private var metadata: Metadata = Metadata.default
    fun metadata(code: MetadataDsl.() -> Unit) {
        metadata = MetadataDsl().also { it.name = DEFAULT_NAME }.apply(code).build()
    }

    /** Add a single container to the pod. */
    fun container(code: ContainerDsl.() -> Unit) {
        containers += ContainerDsl().apply(code).build()
    }

    internal fun build() = Pod(
        apiVersion = apiVersion,
        metadata = metadata,
        spec = PodSpec(containers = containers),
    )
}
