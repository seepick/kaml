package com.github.seepick.kaml.examples.mySetup

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.examples.mySetup.KamlConfig.podLabelKey
import com.github.seepick.kaml.k8s.XK8s
import com.github.seepick.kaml.k8s.artifacts.deployment.deployment
import com.github.seepick.kaml.k8s.artifacts.service.ServiceType
import com.github.seepick.kaml.k8s.artifacts.service.service

private val dbPodLabel = podLabelKey to "${KamlConfig.groupId}-db"

data class DbConfig(
    val user: String,
    val pass: String,
    val port: Int,
    val nodePort: Int = 30_432
)

fun XK8s.dbDeployment(groupId: String, dbConfig: DbConfig) = deployment {
    metadata {
        name = "${groupId}-db-deployment"
        labels += configMap.teamKamlLabel
    }
    selector {
        matchLabels += dbPodLabel
    }
    replicas = 1
    template {
        metadata {
            name = "${groupId}-db-pod"
            labels += dbPodLabel
            labels += configMap.teamKamlLabel
        }
        container {
            name = "${groupId}-db-container"
            image = Image.postgres
            ports {
                containerPort = dbConfig.port
                name = "postgres"
            }
            env += "POSTGRES_USER" to dbConfig.user
            env += "POSTGRES_PASSWORD" to dbConfig.pass
        }
    }
}

fun XK8s.dbService(groupId: String, dbConfig: DbConfig) = service {
    metadata {
        name = configMap.db.serviceName // "${groupId}-db-service"
        labels += configMap.teamKamlLabel
    }
    type = ServiceType.ClusterIP
    selector += dbPodLabel
    ports {
        // which one? StatefulSet? something completely different?
        targetPort = dbConfig.port
        port = dbConfig.port
    }
}
