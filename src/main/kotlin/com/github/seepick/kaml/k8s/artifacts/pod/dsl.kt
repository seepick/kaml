package com.github.seepick.kaml.k8s.artifacts.pod

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.k8s.K8s
import com.github.seepick.kaml.k8s.shared.Container
import com.github.seepick.kaml.k8s.shared.ContainerDsl
import com.github.seepick.kaml.k8s.shared.K8sApiVersion
import com.github.seepick.kaml.k8s.shared.Metadata
import com.github.seepick.kaml.k8s.shared.MetadataDsl

/**
 * The **smallest workable unit** in k8s; basically an abstraction over a container (=a running image).
 *
 * More info: [https://kubernetes.io/docs/concepts/workloads/pods/](https://kubernetes.io/docs/concepts/workloads/pods/)
 */
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
    /**
     * A list of containers running in a single pod.
     *
     * More info: https://kubernetes.io/docs/concepts/workloads/pods/containers/.
     */
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
