package com.github.seepick.kaml.k8s.pod

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.k8s.Container
import com.github.seepick.kaml.k8s.ContainerDsl
import com.github.seepick.kaml.k8s.K8s
import com.github.seepick.kaml.k8s.K8sApiVersion
import com.github.seepick.kaml.k8s.Metadata
import com.github.seepick.kaml.k8s.MetadataDsl

fun K8s.pod(code: PodDsl.() -> Unit): Pod =
    PodDsl().apply(code).build()

abstract class PodOrTemplateDsl<POT>() {

    protected var _metadata: Metadata = Metadata.default
        private set

    fun metadata(code: MetadataDsl.() -> Unit) {
//      TODO ?  require(metadata == null) { "deployment template metadata already set" }
        _metadata = MetadataDsl().apply(code).build()
    }

    protected val containers = mutableListOf<Container>()
    /** A list of pods running in a single container. */
    fun container(code: ContainerDsl.() -> Unit) {
        containers += ContainerDsl().apply(code).build()
    }

    internal abstract fun build(): POT
}

@KamlDsl
class PodDsl : PodOrTemplateDsl<Pod>() {

    /** Read-only. Defaults to "v1". */
    val apiVersion = K8sApiVersion.Pod

    override fun build() = Pod(
        apiVersion = apiVersion,
        metadata = _metadata,
        spec = PodSpec(containers = containers),
    )
}
