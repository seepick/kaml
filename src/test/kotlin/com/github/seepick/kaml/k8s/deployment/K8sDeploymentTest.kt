package com.github.seepick.kaml.k8s.deployment

import com.github.seepick.kaml.Kaml
import com.github.seepick.kaml.KamlException
import com.github.seepick.kaml.k8s.k8s
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.string.shouldContain

class K8sDeploymentTest : StringSpec({
    "required missing" {
        shouldThrow<KamlException> {
            Kaml.k8s.deployment {
            }
        }
    }
    "minimum" {
        Kaml.k8s.deployment {
            template {
                metadataLabelsApp = "test-template"
            }
        }.toYaml() shouldContain "test-template"
    }
})
