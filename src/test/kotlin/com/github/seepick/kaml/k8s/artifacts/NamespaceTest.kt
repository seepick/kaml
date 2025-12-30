package com.github.seepick.kaml.k8s.artifacts

import com.github.seepick.kaml.Kaml
import com.github.seepick.kaml.KamlException
import com.github.seepick.kaml.KamlKonfig
import com.github.seepick.kaml.k8s.k8s
import com.github.seepick.kaml.validation.ValidationLevel
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.string.shouldContainIgnoringCase

class NamespaceTest : StringSpec({
    "empty throws" {
        shouldThrow<KamlException> {
            Kaml.k8s.namespace(konfig = KamlKonfig(ValidationLevel.FailOnError)) {
            }
        }.message shouldContainIgnoringCase "Namespace name must not be null or empty"
    }
    "sun" {
        Kaml.k8s.namespace {
            metadata {
                name = "my-ns"
            }
        }.toYaml() shouldBeEqual """
            apiVersion: v1
            kind: Namespace
            metadata:
              name: my-ns
        """.trimIndent()
    }
})