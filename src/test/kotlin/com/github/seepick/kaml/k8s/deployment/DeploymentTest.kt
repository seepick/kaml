package com.github.seepick.kaml.k8s.deployment

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.Kaml
import com.github.seepick.kaml.k8s.k8s
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.equals.shouldBeEqual

class DeploymentTest : DescribeSpec({
    describe("general tests") {
        it("empty should not throw") {
            Kaml.k8s.deployment {
            }
        }
        it("full deployment") {
            Kaml.k8s.deployment {
                metadata {
                    name = "my-deployment"
                }
                selector {
                    matchLabels += "app" to "my-container"
                }
                replicas = 2
                // TODO strategy: type: RollingUpdate: maxUnavailable: 1 / maxSurge: 1
                // minReadySeconds: 10
                template {
                    metadata {
                        labels += "app" to "my-container"
                    }
                    container {
                        name = "my-container"
                        image = Image(name = "my-image", version = "latest")
                        // TODO ports: - containerPort: 80
                    }
                }
            }.toYamlString() shouldBeEqual """
                apiVersion: apps/v1
                kind: Deployment
                metadata:
                  name: my-deployment
                spec:
                  replicas: 2
                  selector:
                    matchLabels:
                      app: my-container
                  template:
                    metadata:
                      labels:
                        app: my-container
                    spec:
                      containers:
                        - name: my-container
                          image: my-image:latest
            """.trimIndent()
        }
    }
})
