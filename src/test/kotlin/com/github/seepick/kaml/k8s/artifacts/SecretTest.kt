package com.github.seepick.kaml.k8s.artifacts

import com.github.seepick.kaml.Kaml
import com.github.seepick.kaml.k8s.k8s
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equals.shouldBeEqual

class SecretTest : StringSpec({
    "sunshine case" {
        Kaml.k8s.secret {
            metadata {
                name = "my-secret"
            }
            data += "MY_KEY" to "my-value"
        }.toYaml() shouldBeEqual """
            apiVersion: v1
            kind: Secret
            metadata:
              name: my-secret
            data:
              MY_KEY: my-value
        """.trimIndent()
    }
})