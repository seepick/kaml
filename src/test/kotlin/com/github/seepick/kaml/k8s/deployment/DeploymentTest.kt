package com.github.seepick.kaml.k8s.deployment

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.Kaml
import com.github.seepick.kaml.KamlException
import com.github.seepick.kaml.k8s.k8s
import com.github.seepick.kaml.loadTestResource
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.string.shouldContain

class DeploymentTest : DescribeSpec({
    describe("simple tests") {
        it("required missing") {
            shouldThrow<KamlException> {
                Kaml.k8s.deployment {
                }
            }
        }
        it("minimum") {
            Kaml.k8s.deployment {
                template {
                    metadataLabelsApp = "test-template"
                }
            }.toYaml() shouldContain "test-template"
        }
    }
    describe("file based tests") {
        fun loadResource(path: String): String = loadTestResource("/k8s/$path")
        it("deployment-simple.yaml") {
            Kaml.k8s.deployment {
                name = "my-deployment"
                replicas = 2
                selectorMatchLabelsApp = "my-container"
                template {
                    metadataLabelsApp = "my-container"
                    container {
                        name = "my-container"
                        image = Image(name = "my-image", version = "latest")
                    }
                }
            }.toYamlString() shouldBeEqual loadResource("deployment-simple.yaml")
        }
    }
})
