package com.github.seepick.kaml.examples.mySetup

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.k8s.XK8s
import com.github.seepick.kaml.k8s.artifacts.deployment.deployment
import com.github.seepick.kaml.k8s.artifacts.service.ServiceType
import com.github.seepick.kaml.k8s.artifacts.service.service
import com.github.seepick.kaml.k8s.shared.Gi
import com.github.seepick.kaml.k8s.shared.Mi
import com.github.seepick.kaml.k8s.shared.milliCpu

private val artifactId = "backend"
private val demoAppVersion = "2" // "latest"
private val Image.Companion.demoApp get() = Image("docker.io/library/demo-app", version = demoAppVersion)
private val podLabel = AppConfig.labels.podLabelKey to "${AppConfig.groupId}-backend"

fun XK8s.backendDeployment(configMapRef: String, backendPort: Int) = deployment {
    // TODO "kubectl.kubernetes.io/last-applied-configuration" annotation?
    metadata {
        name = "${appConfig.groupId}-$artifactId-deployment"
        labels += appConfig.labels.teamKamlLabel
    }
    selector {
        matchLabels += podLabel
    }
    // TODO scale based on load
    replicas = 3
    template {
        metadata {
            name = "${appConfig.groupId}-$artifactId-pod"
            labels += podLabel
            labels += appConfig.labels.teamKamlLabel
        }
        container {
            name = "${appConfig.groupId}-$artifactId-container"
            image = Image.demoApp
            env {
                configMaps += configMapRef
                values += "PORT" to "\"\"$backendPort\"\"" // FIXME quote fix?! otherwise "cannot convert int64 to string"
            }
            resources {
                requests {
                    cpu = 100.milliCpu
                    memory = 512.Mi
                }
                limits {
                    cpu = 500.milliCpu
                    memory = 1.Gi
                }
            }
        }
    }
    // TODO ready/health probes

    // $ k exec -it kaml-backend-deployment-7849768597-5r8wq -- /bin/bash
    // $ curl localhost:8080
}

fun XK8s.backendService(backendPort: Int) = service {
    metadata {
        name = "${appConfig.groupId}-$artifactId-service"
        labels += appConfig.labels.teamKamlLabel
    }
    type = ServiceType.NodePort // implicitly also a load balancer
    selector += podLabel
    ports {
        targetPort = backendPort // TODO correct port?!
        port = 8080
        nodePort = 30080
    }
    // $ minikube service kaml-backend-service --url
    // ... prints the current URL
    // might block your terminal, otherwise also possible:
    // $ curl $(minikube service kaml-backend-service --url)
}
