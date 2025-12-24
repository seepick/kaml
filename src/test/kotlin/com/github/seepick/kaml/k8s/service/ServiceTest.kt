package com.github.seepick.kaml.k8s.service

import com.github.seepick.kaml.Kaml
import com.github.seepick.kaml.k8s.Protocol
import com.github.seepick.kaml.k8s.k8s
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equals.shouldBeEqual

class ServiceTest : StringSpec({
    "Simple" {
        Kaml.k8s.service {
            metadata {
                name = "my-service"
            }
            serviceType = ServiceType.NodePort
            ports {
                port = 80
                targetPort = 80
                nodePort = 30080
                protocol = Protocol.TCP
            }
            selector += "app" to "my-pod-label"
        }.toYamlString() shouldBeEqual """
            apiVersion: v1
            kind: Service
            metadata:
              name: my-service
            spec:
              type: NodePort
              ports:
                - protocol: TCP
                  port: 80
                  targetPort: 80
                  nodePort: 30080
              selector:
                app: my-pod-label
        """.trimIndent()
    }
})