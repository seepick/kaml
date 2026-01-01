package com.github.seepick.kaml.k8s.artifacts.pod

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.KamlKonfig
import com.github.seepick.kaml.k8s.K8s
import com.github.seepick.kaml.k8s.XK8s
import com.github.seepick.kaml.k8s.shared.Container
import com.github.seepick.kaml.k8s.shared.ContainerDsl
import com.github.seepick.kaml.k8s.shared.HostPath
import com.github.seepick.kaml.k8s.shared.HostPathDsl
import com.github.seepick.kaml.k8s.shared.K8sApiVersion
import com.github.seepick.kaml.k8s.shared.Metadata
import com.github.seepick.kaml.k8s.shared.MetadataDsl
import com.github.seepick.kaml.k8s.shared.Volume
import com.github.seepick.kaml.k8s.shared.VolumeDsl
import com.github.seepick.kaml.validation.DomainBuilder
import com.github.seepick.kaml.validation.Validatable
import com.github.seepick.kaml.validation.buildValidated

/**
 * The **smallest workable unit** in k8s; basically an abstraction over a container (=a running image).
 *
 * More info: [https://kubernetes.io/docs/concepts/workloads/pods/](https://kubernetes.io/docs/concepts/workloads/pods/)
 */
fun K8s.pod(konfig: KamlKonfig = KamlKonfig.default, code: PodDsl.() -> Unit) =
    PodDsl().apply(code).buildValidated(konfig)

fun XK8s.pod(code: PodDsl.() -> Unit) =
    K8s.pod(konfig, code)


abstract class PodOrTemplateDsl<POT : Validatable>() : DomainBuilder<POT> {

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

    protected val volumes = mutableListOf<Volume>()
    fun volume(code: VolumeDsl.() -> Unit) {
        volumes += VolumeDsl().apply(code).build()
    }

    protected var hostPath: HostPath? = null
    /** Use it only in a single node cluster! */
    fun hostPath(code: HostPathDsl.() -> Unit) {
        hostPath = HostPathDsl().apply(code).build()
    }

    var restartPolicy: RestartPolicy? = null

    protected fun buildPodSpec() = PodSpec(
        containers = containers,
        restartPolicy = restartPolicy,
        volumes = volumes,
        hostPath = hostPath,
    )
}

@KamlDsl
class PodDsl : PodOrTemplateDsl<Pod>(), DomainBuilder<Pod> {

    /** Read-only. Defaults to "v1". */
    val apiVersion = K8sApiVersion.v1

    override fun build() = Pod(
        apiVersion = apiVersion,
        metadata = _metadata,
        spec = buildPodSpec(),
    )
}
