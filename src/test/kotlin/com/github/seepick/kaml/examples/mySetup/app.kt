package com.github.seepick.kaml.examples.mySetup

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.k8s.XK8s
import com.github.seepick.kaml.k8s.deployment.deployment

val Image.Companion.demoApp get() = Image("docker.io/library/demo-app", version = "1")

fun XK8s.backendDeployment(groupId: String) = deployment {
    val podLabel = configMap.podLabelKey to "${groupId}-backend"
    // TODO "kubectl.kubernetes.io/last-applied-configuration" annotation?
    metadata {
        name = "${groupId}-backend-deployment"
    }
    selector {
        matchLabels += podLabel
    }
    replicas = 3
    template {
        metadata {
            name = "${groupId}-backend-pod"
            labels += podLabel
        }
        container {
            name = "${groupId}-backend-container"
            image = Image.demoApp
            env += "PORT" to "\"\"8080\"\"" // FIXME quote fix?! otherwise "cannot convert int64 to string"
        }
    }
    // k exec -it kaml-backend-deployment-7849768597-5r8wq -- /bin/bash
    // curl localhost:8080
    // FIXME wire db service
    // TODO ready/health probes
}

// FIXME enable load balancer service