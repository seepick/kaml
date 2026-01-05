package com.github.seepick.kaml.k8s.kustomize

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.k8s.artifacts.deployment.Deployment

data class Base(
    val deployments: List<Deployment>
)

@KamlDsl
class BaseDsl {

    private val deployments = mutableListOf<Deployment>()
    fun add(deployment: Deployment) {
        deployments += deployment
    }

    fun build() = Base(
        deployments = deployments,
    )
}
