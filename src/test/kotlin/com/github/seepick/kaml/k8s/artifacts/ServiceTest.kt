package com.github.seepick.kaml.k8s.artifacts

import com.github.seepick.kaml.Kaml
import com.github.seepick.kaml.KamlKonfig
import com.github.seepick.kaml.k8s.artifacts.service.ServiceType
import com.github.seepick.kaml.k8s.artifacts.service.service
import com.github.seepick.kaml.k8s.k8s
import com.github.seepick.kaml.k8s.shared.Protocol
import com.github.seepick.kaml.validation.KamlValidationException
import com.github.seepick.kaml.validation.ValidationLevel
import com.github.seepick.kaml.validation.ValidationSeverity
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.shouldForOne
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.string.shouldContain

class ServiceTest : StringSpec({
    "invalid node port" {
        shouldThrow<KamlValidationException> {
            Kaml.k8s.service(KamlKonfig(ValidationLevel.FailOnError)) {
                ports {
                    port = 80 // required
                    nodePort = 222
                }
            }
        }.validationResult.issues.shouldForOne {
            it.message shouldContain "222"
            it.message shouldContain "32767"
            it.severity shouldBeEqual ValidationSeverity.Warning
        }
    }
    "Simple" {
        Kaml.k8s.service {
            metadata {
                name = "my-service"
            }
            type = ServiceType.NodePort
            ports {
                port = 80
                targetPort = 80
                nodePort = 30080
                protocol = Protocol.TCP
            }
            selector += "app" to "my-pod-label"
        }.toYaml() shouldBeEqual """
            apiVersion: v1
            kind: Service
            metadata:
              name: my-service
            spec:
              type: NodePort
              ports:
                - protocol: TCP
                  nodePort: 30080
                  port: 80
                  targetPort: 80
              selector:
                app: my-pod-label
        """.trimIndent()
    }
})