package com.github.seepick.kaml.examples

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.Kaml
import com.github.seepick.kaml.k8s.artifacts.deployment.deployment
import com.github.seepick.kaml.k8s.artifacts.service.service
import com.github.seepick.kaml.k8s.k8s
import com.github.seepick.kaml.k8s.shared.Protocol

object Examples {

    // save all together in one big file (with "----")
    // save each to another file/directory (for `kubectl apply -f .`)

    val dbServiceName = "my-db-service" // equals hostname
    val postgresCreds = "postgres" to "postgres"

    val dbTypeLabel = "type" to "db"
    val dbDeployment = Kaml.k8s.deployment {
        metadata {
            name = "my-db-deployment"
        }
        selector {
            matchLabels += dbTypeLabel
        }
        replicas = 2
        template {
            metadata {
                name = "my-db-pod"
                labels += dbTypeLabel
            }
            container {
                name = "my-postgres-container"
                image = Image.Companion("postgres", version = "15-alpine")
                ports {
                    containerPort = 5432
                    name = "postgres"
                }
                env {
                    values += "POSTGRES_USER" to postgresCreds.first // use configmap/secrets instead
                    values += "POSTGRES_PASSWORD" to postgresCreds.second
                }
            }
        }
    }

    val backendTypeLabel = "type" to "backend"
    val backendDeployment = Kaml.k8s.deployment {
        metadata {
            name = "my-backend-deployment"
        }
        selector {
            matchLabels += backendTypeLabel
        }
        replicas = 5
        template {
            metadata {
                name = "my-backend-pod"
                labels += backendTypeLabel
            }
            container {
                name = "my-backend-container"
                image = Image.Companion("backend-app", version = "1")
                ports {
                    containerPort = 8080
                    name = "backend-port"
                }
                env {
                    values += "POSTGRES_HOST" to dbServiceName
                    values += "POSTGRES_USER" to postgresCreds.first // use configmap/secrets instead
                    values += "POSTGRES_PASSWORD" to postgresCreds.second
                }
            }
        }
    }

    // TODO wire both together via service
    val dbService = Kaml.k8s.service {
        metadata {
            name = dbServiceName
        }
        ports {
            port = 5432
            targetPort = 5432
            protocol = Protocol.TCP
        }
        selector += dbTypeLabel
    }
}