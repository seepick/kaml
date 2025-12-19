package com.github.seepick.kaml.k8s.pod

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.Kaml
import com.github.seepick.kaml.k8s.k8s
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.equals.shouldBeEqual

class PodTest : DescribeSpec({
    describe("simple test") {
        it("test") {
            Kaml.k8s.pod {
                name = "my-pod"
                labels = mapOf("foo" to "bar")
                container {
                    name = "my-container"
                    image = Image(name = "my-image", version = "latest")
                }
            }.toYaml() shouldBeEqual """
                apiVersion: v1
                kind: Pod
                metadata:
                  name: my-pod
                  labels:
                    foo: bar
                spec:
                  containers:
                    - name: my-container
                      image: my-image:latest
            """.trimIndent()
        }
    }
})