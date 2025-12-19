package com.github.seepick.kaml.k8s.deployment

import com.github.seepick.kaml.GenericImage
import com.github.seepick.kaml.Kaml
import com.github.seepick.kaml.k8s.k8s
import com.github.seepick.kaml.loadTestResource
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equals.shouldBeEqual

class K8sDeployIntegrationTest : StringSpec({

    fun loadResource(path: String): String = loadTestResource("/k8s/$path")

    "deployment-simple.yaml" {
        Kaml.k8s.deployment {
            name = "my-deployment"
            replicas = 2
            selectorMatchLabelsApp = "my-container"
            template {
                metadataLabelsApp = "my-container"
                container {
                    name = "my-container"
                    image = GenericImage(name = "my-image", version = "latest")
                }
            }
        }.toYamlString() shouldBeEqual loadResource("deployment-simple.yaml")
    }
})