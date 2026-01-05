package com.github.seepick.kaml.k8s.kustomize

import com.github.seepick.kaml.Kaml
import com.github.seepick.kaml.k8s.artifacts.deployment.Deployment
import com.github.seepick.kaml.k8s.artifacts.deployment.replicas
import com.github.seepick.kaml.k8s.artifacts.deployment.spec
import com.github.seepick.kaml.k8s.deployment
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.string.shouldContain
import io.kotest.property.Arb
import io.kotest.property.arbitrary.next

class OverlayTest : DescribeSpec({

    val overlayName = OverlayName("test-overlay")

    describe("change") {
        it("replica from 1 to 2") {
            val oldReplicas = 1
            val newReplicas = oldReplicas + 1
            val deployment = Arb.deployment().next().let {
                Deployment.spec.replicas.set(it, oldReplicas)
            }

            val world = Kaml.kustomize {
                base {
                    add(deployment)
                }
                overlay(overlayName) {
                    change(deployment) {
                        Deployment.spec.replicas.set(it, newReplicas)
                    }
                }
            }

            world.generate(overlayName) shouldContain """
                spec:
                  replicas: $newReplicas
            """.trimIndent()
        }
    }
})