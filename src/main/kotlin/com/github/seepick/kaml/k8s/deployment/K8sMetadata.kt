package com.github.seepick.kaml.k8s.deployment


data class PodSpec(
    val containers: List<Container>,
)