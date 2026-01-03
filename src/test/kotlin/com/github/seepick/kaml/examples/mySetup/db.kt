package com.github.seepick.kaml.examples.mySetup

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.k8s.XK8s
import com.github.seepick.kaml.k8s.artifacts.deployment.deployment
import com.github.seepick.kaml.k8s.artifacts.service.ServiceType
import com.github.seepick.kaml.k8s.artifacts.service.service
import com.github.seepick.kaml.k8s.shared.Mi
import com.github.seepick.kaml.k8s.shared.milliCpu

private val dbPodLabel = AppConfig.labels.podLabelKey to "${AppConfig.groupId}-db"

data class DbConfig(
    val user: String,
    val pass: String,
    val port: Int,
    val nodePort: Int = 30_432
)

// FIXME use a StatefulSet instead! to sync IO
fun XK8s.dbDeployment(groupId: String, dbConfig: DbConfig) = deployment {
    metadata {
        name = "${groupId}-db-deployment"
        labels += appConfig.labels.teamKaml
    }
    selector {
        matchLabels += dbPodLabel
    }
    replicas = 1
    template {
        metadata {
            name = "${groupId}-db-pod"
            labels += dbPodLabel
            labels += appConfig.labels.teamKaml
        }
        container {
            name = "${groupId}-db-container"
            image = Image.postgres
            ports {
                containerPort = dbConfig.port
                name = "postgres"
            }
            env {
                values += "POSTGRES_USER" to dbConfig.user
                values += "POSTGRES_PASSWORD" to dbConfig.pass
            }
            resources {
                requests {
                    cpu = 100.milliCpu
                    memory = 256.Mi
                }
                limits {
                    cpu = 500.milliCpu
                    memory = 512.Mi
                }
            }
        }
    }
}

fun XK8s.dbService(groupId: String, dbConfig: DbConfig) = service {
    metadata {
        name = appConfig.db.serviceName // "${groupId}-db-service"
        labels += appConfig.labels.teamKaml
    }
    type = ServiceType.ClusterIP
    selector += dbPodLabel
    ports {
        // which one? StatefulSet? something completely different?
        targetPort = dbConfig.port
        port = dbConfig.port
    }
}
