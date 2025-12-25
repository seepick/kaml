package com.github.seepick.kaml.examples.mySetup

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.examples.mySetup.K8sConfigMap.podLabelKey
import com.github.seepick.kaml.k8s.XK8s
import com.github.seepick.kaml.k8s.deployment.deployment

data class DbDeploymentConfig(val postgresUser: String, val postgresPass: String)

fun XK8s.dbDeployment(groupId: String, config: DbDeploymentConfig) = deployment {
    val podLabel = podLabelKey to "${groupId}-db"

    metadata {
        name = "${groupId}-db-deployment"
    }
    selector {
        matchLabels += podLabel
    }
    replicas = 2
    template {
        metadata {
            name = "${groupId}-db-pod"
            labels += podLabel
        }
        container {
            name = configMap.dbHostname
            image = Image("postgres", version = "15-alpine")
            ports {
                containerPort = 5432
                name = "postgres"
            }
            env += "POSTGRES_USER" to config.postgresUser
            env += "POSTGRES_PASSWORD" to config.postgresPass
        }
    }
}
