package com.github.seepick.kaml.examples.mySetup

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.k8s.XK8s
import com.github.seepick.kaml.k8s.artifacts.deployment.deployment
import com.github.seepick.kaml.k8s.artifacts.service.ServiceType
import com.github.seepick.kaml.k8s.artifacts.service.service

private val artifactId = "frontend"
private val Image.Companion.frontend get() = Image("docker.io/library/demo-$artifactId", version = "latest")
private val podLabel = KamlConfig.podLabelKey to "${KamlConfig.groupId}-$artifactId"

fun XK8s.frontendDeployment(backendServiceHostAndPort: String) = deployment {
    metadata {
        name = "${configMap.groupId}-$artifactId-deployment"
        labels += configMap.teamKamlLabel
    }
    selector {
        matchLabels += podLabel
    }
    replicas = 3
    template {
        metadata {
            name = "${configMap.groupId}-$artifactId-pod"
            labels += podLabel
            labels += configMap.teamKamlLabel
        }
        container {
            name = "${configMap.groupId}-$artifactId-container"
            image = Image.frontend
            env += "BACKEND_URL" to "\"\"https://$backendServiceHostAndPort\"\""
        }
    }
}

fun XK8s.frontendService() = service {
    metadata {
        name = "${configMap.groupId}-$artifactId-service"
        labels += configMap.teamKamlLabel
    }
    // FIXME check type and ports
    type = ServiceType.NodePort // implicitly also a load balancer
    selector += podLabel
    ports {
        targetPort = 80
        port = 80
        nodePort = 30080
    }
}
