package com.github.seepick.kaml.examples.mySetup

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.k8s.XK8s
import com.github.seepick.kaml.k8s.artifacts.deployment.deployment
import com.github.seepick.kaml.k8s.artifacts.service.ServiceType
import com.github.seepick.kaml.k8s.artifacts.service.service
import com.github.seepick.kaml.k8s.shared.Mi
import com.github.seepick.kaml.k8s.shared.milliCpu

private val artifactId = "frontend"
private val Image.Companion.frontend get() = Image("docker.io/library/demo-$artifactId", version = "latest")
private val podLabel = AppConfig.labels.podLabelKey to "${AppConfig.groupId}-$artifactId"

fun XK8s.frontendDeployment(backendServiceHostAndPort: String) = deployment {
    metadata {
        name = "${appConfig.groupId}-$artifactId-deployment"
        labels += appConfig.labels.teamKamlLabel
    }
    selector {
        matchLabels += podLabel
    }
    replicas = 3
    template {
        metadata {
            name = "${appConfig.groupId}-$artifactId-pod"
            labels += podLabel
            labels += appConfig.labels.teamKamlLabel
        }
        container {
            name = "${appConfig.groupId}-$artifactId-container"
            image = Image.frontend
            env {
                values += "BACKEND_URL" to "\"\"https://$backendServiceHostAndPort\"\""
            }
            resources {
                requests {
                    cpu = 100.milliCpu
                    memory = 64.Mi
                }
                limits {
                    cpu = 500.milliCpu
                    memory = 128.Mi
                }
            }
        }
    }
}

fun XK8s.frontendService() = service {
    metadata {
        name = "${appConfig.groupId}-$artifactId-service"
        labels += appConfig.labels.teamKamlLabel
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
