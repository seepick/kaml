package com.github.seepick.kaml.k8s.deployment

import com.github.seepick.kaml.Kaml
import com.github.seepick.kaml.k8s.deployment.dsl.deployment
import com.github.seepick.kaml.k8s.deployment.yaml.toYamlString
import com.github.seepick.kaml.k8s.k8s
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.string.shouldContain

class K8sDeploymentTest : StringSpec ({
    "empty" {
        Kaml.k8s.deployment {}.toYamlString() shouldBeEqual """
            apiVersion: apps/v1
            kind: Deployment
            metadata:
              name: default-deployment-name
        """.trimIndent()
    }
    "set name" {
        Kaml.k8s.deployment {
            name = "test-deployment"
        }.toYamlString() shouldContain "metadata:\n  name: test-deployment"
    }
})
