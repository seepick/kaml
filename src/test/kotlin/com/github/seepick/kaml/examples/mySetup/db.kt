package com.github.seepick.kaml.examples.mySetup

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.examples.mySetup.K8sConfigMap.podLabelKey
import com.github.seepick.kaml.k8s.XK8s
import com.github.seepick.kaml.k8s.deployment.deployment
import com.github.seepick.kaml.k8s.service.ServiceType
import com.github.seepick.kaml.k8s.service.service

private val podLabel = podLabelKey to "${groupId}-db"

data class DbDeploymentConfig(
    val user: String,
    val pass: String,
    val port: Int,
)

fun XK8s.dbDeployment(groupId: String, config: DbDeploymentConfig) = deployment {
    metadata {
        name = "${groupId}-db-deployment"
    }
    selector {
        matchLabels += podLabel
    }
    replicas = 1
    template {
        metadata {
            name = "${groupId}-db-pod"
            labels += podLabel
        }
        container {
            name = configMap.dbHostname
            image = Image("postgres", version = "15-alpine")
            ports {
                containerPort = config.port
                name = "postgres"
            }
            env += "POSTGRES_USER" to config.user
            env += "POSTGRES_PASSWORD" to config.pass
        }
    }
}

fun XK8s.dbService(groupId: String) = service {
    metadata {
        name = "${groupId}-db-service"
    }
    type = ServiceType.ClusterIP
    selector += podLabel
    ports {
        // FIXME which one? StatefulSet? something completely different?
//        targetPort = 8080
//        port = 8080
//        nodePort = 30080
    }
}
