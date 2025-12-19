package com.github.seepick.kaml.k8s.deployment.dsl

import com.github.seepick.kaml.k8s.K8s
import com.github.seepick.kaml.k8s.deployment.domain.K8sDeployment

fun K8s.deployment(code: () -> Unit): K8sDeployment {
    return K8sDeployment("fixed")
}
