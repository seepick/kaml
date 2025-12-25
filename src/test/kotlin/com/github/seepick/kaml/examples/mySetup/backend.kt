package com.github.seepick.kaml.examples.mySetup

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.k8s.XK8s
import com.github.seepick.kaml.k8s.artifacts.deployment.deployment
import com.github.seepick.kaml.k8s.artifacts.service.ServiceType
import com.github.seepick.kaml.k8s.artifacts.service.service

private val artifactId = "backend"
private val Image.Companion.demoApp get() = Image("docker.io/library/demo-app", version = "latest")
private val podLabel = KamlConfigMap.podLabelKey to "${KamlConfigMap.groupId}-backend"

fun XK8s.backendDeployment() = deployment {
    // TODO "kubectl.kubernetes.io/last-applied-configuration" annotation?
    metadata {
        name = "${configMap.groupId}-$artifactId-deployment"
    }
    selector {
        matchLabels += podLabel
    }
    // TODO scale based on load
    replicas = 3
    template {
        metadata {
            name = "${configMap.groupId}-$artifactId-pod"
            labels += podLabel
        }
        container {
            name = "${configMap.groupId}-$artifactId-container"
            image = Image.demoApp
            env += "PORT" to "\"\"8080\"\"" // FIXME quote fix?! otherwise "cannot convert int64 to string"
            env += "DB_JDBC" to configMap.dbJdbc
            env += "DB_USER" to configMap.dbUserPass.first
            env += "DB_PASS" to configMap.dbUserPass.second
        }
    }
    // FIXME wire db service
    // TODO ready/health probes

    // $ k exec -it kaml-backend-deployment-7849768597-5r8wq -- /bin/bash
    // $ curl localhost:8080
}

fun XK8s.backendService(backendPort: Int) = service {
    metadata {
        name = "${configMap.groupId}-$artifactId-service"
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
