package com.github.seepick.kaml.k8s.deployment

import com.github.seepick.kaml.Kaml
import com.github.seepick.kaml.github.yaml.toYamlString
import com.github.seepick.kaml.k8s.deployment.dsl.deployment
import com.github.seepick.kaml.k8s.deployment.yaml.toYamlString
import com.github.seepick.kaml.k8s.k8s
import com.github.seepick.kaml.loadTestResource
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equals.shouldBeEqual

class K8sDeployIntegrationTest : StringSpec({

    fun loadResource(path: String): String = loadTestResource("/k8s/$path")

    "deployment matches".config(enabled = false) {
        // FIXME implement me
        Kaml.k8s.deployment {
        }.toYamlString() shouldBeEqual loadResource("deploy-simple.yaml")
    }
})