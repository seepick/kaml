package com.github.seepick.kaml.k8s.pod

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.k8s.K8s
import com.github.seepick.kaml.k8s.K8sApiVersion
import com.github.seepick.kaml.k8s.PodOrTemplateDsl

fun K8s.pod(code: PodDsl.() -> Unit): Pod =
    PodDsl().apply(code).build()

@KamlDsl
class PodDsl : PodOrTemplateDsl<Pod>() {

    /** Read-only. Defaults to "v1". */
    val apiVersion = K8sApiVersion.Pod

    override fun build() = Pod(
        apiVersion = apiVersion,
        metadata = metadata,
        spec = PodSpec(containers = containers),
    )
}
