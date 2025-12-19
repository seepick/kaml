package com.github.seepick.kaml.k8s.pod

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.k8s.ContainerDsl
import com.github.seepick.kaml.k8s.GeneralMetadata
import com.github.seepick.kaml.k8s.K8s
import com.github.seepick.kaml.k8s.deployment.Container
import com.github.seepick.kaml.k8s.deployment.PodSpec

fun K8s.pod(code: PodDsl.() -> Unit): Pod =
    PodDsl().apply(code).build()

@KamlDsl
class PodDsl {

    /** Read-only. Defaults to "v1". */
    val apiVersion = "v1"

    /** The visible label of the pod. */
    var name = "default-pod-name"

    /** Arbitrary key-value information; for grouping/selecting. */
    var labels = emptyMap<String, String>()

    private val containers = mutableListOf<Container>()

    /** Add a single container to the pod. */
    fun container(code: ContainerDsl.() -> Unit) {
        containers += ContainerDsl().apply(code).build()
    }

    internal fun build() = Pod(
        apiVersion = apiVersion,
        metadata = GeneralMetadata(name = name, labels = labels),
        spec = PodSpec(containers = containers),
    )
}
