package com.github.seepick.kaml.k8s.artifacts

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.Kaml
import com.github.seepick.kaml.KamlKonfig
import com.github.seepick.kaml.KamlValidationException
import com.github.seepick.kaml.ValidationLevel
import com.github.seepick.kaml.k8s.artifacts.deployment.deployment
import com.github.seepick.kaml.k8s.k8s
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.should
import io.kotest.matchers.string.shouldContainIgnoringCase


class DeploymentTest : DescribeSpec({
    describe("general tests") {
        it("empty should not throw Because validation disabled by default") {
            Kaml.k8s.deployment {}
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
            }.toYaml() shouldBeEqual """
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
    describe("Given strictness enabled") {
        val strictKonfig = KamlKonfig(ValidationLevel.FailOnError)
        it("When no container Then throw") {
            shouldThrow<KamlValidationException> {
                Kaml.k8s.deployment(strictKonfig) {
                    template {
                        // no containers
                    }
                }
            }.validationResult.issues should {
                it.size shouldBeEqual 1
                it.first().message shouldContainIgnoringCase "template must contain at least one container"
            }
        }
        it("When no container name Then throw") {
            shouldThrow<KamlValidationException> {
                Kaml.k8s.deployment(strictKonfig) {
                    template {
                        container {
                            image = Image.nginx
//                            name = "nope"
                        }
                    }
                }
            }.validationResult.issues should {
                it.size shouldBeEqual 1
                it.first().message shouldContainIgnoringCase "container name must not be empty"
            }
        }
        it("When valid Then ok") {
            Kaml.k8s.deployment(strictKonfig) {
                template {
                    container {
                        image = Image.nginx
                        name = "nginx"
                    }
                }
            }
        }
    }
})
