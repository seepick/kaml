package com.github.seepick.kaml.k8s.artifacts

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.Kaml
import com.github.seepick.kaml.k8s.artifacts.pod.pod
import com.github.seepick.kaml.k8s.k8s
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.equals.shouldBeEqual

class PodTest : DescribeSpec({
    describe("simple test") {
        it("test") {
            Kaml.k8s.pod {
                metadata {
                    name = "my-podname"
                    namespace = "dev"
                    labels += "myLabel" to "myLabelValue"
                }
                container {
                    name = "my-containername"
                    image = Image.Companion(name = "my-imagename", version = "my-imageVersion")
                    ports {
                        containerPort = 80
                        name = "my-portname"
                    }
                    env += "envKey" to "envVal"
                }
            }.toYaml() shouldBeEqual """
                apiVersion: v1
                kind: Pod
                metadata:
                  name: my-podname
                  namespace: dev
                  labels:
                    myLabel: myLabelValue
                spec:
                  containers:
                    - name: my-containername
                      image: my-imagename:my-imageVersion
                      ports:
                        - name: my-portname
                          containerPort: 80
                      env:
                        - name: envKey
                          value: envVal
            """.trimIndent()
        }
    }
})