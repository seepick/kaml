package com.github.seepick.kaml.k8s.kustomize

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.Kaml
import com.github.seepick.kaml.k8s.artifacts.deployment.Deployment
import com.github.seepick.kaml.k8s.artifacts.deployment.deployment
import com.github.seepick.kaml.k8s.artifacts.deployment.replicas
import com.github.seepick.kaml.k8s.artifacts.deployment.spec
import com.github.seepick.kaml.k8s.k8s

fun playground() {
    val backendDeployment = Kaml.k8s.deployment {
        metadata {
            name = "backend-deployment"
        }
        replicas = 3
        template {
            metadata {
                name = "backend-pod"
            }
            container {
                name = "backend-container"
                image = Image.nginx
            }
        }
    }

    val devOverlay = OverlayName("dev")
    val world = Kaml.kustomize {
        base {
            add(backendDeployment)
        }
        overlay(devOverlay) {
            change(backendDeployment) {
                Deployment.spec.replicas.set(it, 1)
            }
        }
    }
    world.generate(devOverlay)
}
