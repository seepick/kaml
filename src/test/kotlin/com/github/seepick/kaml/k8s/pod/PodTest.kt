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
                metadata {
                    name = "my-podname"
                    labels += "myLabel" to "myLabelValue"
                }
                container {
                    name = "my-containername"
                    image = Image(name = "my-imagename", version = "my-imageVersion")
                    ports {
                        containerPort = 80
                        name = "my-portname"
                    }
                }
            }.toYaml() shouldBeEqual """
                apiVersion: v1
                kind: Pod
                metadata:
                  name: my-podname
                  labels:
                    myLabel: myLabelValue
                spec:
                  containers:
                    - name: my-containername
                      image: my-imagename:my-imageVersion
                      ports:
                        - containerPort: 80
                          name: my-portname
            """.trimIndent()
        }
    }
})