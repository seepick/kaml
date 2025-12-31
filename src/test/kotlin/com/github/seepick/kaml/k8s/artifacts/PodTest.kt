package com.github.seepick.kaml.k8s.artifacts

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.Kaml
import com.github.seepick.kaml.k8s.any
import com.github.seepick.kaml.k8s.artifacts.pod.RestartPolicy
import com.github.seepick.kaml.k8s.artifacts.pod.pod
import com.github.seepick.kaml.k8s.k8s
import com.github.seepick.kaml.k8s.shared.Gi
import com.github.seepick.kaml.k8s.shared.Mi
import com.github.seepick.kaml.k8s.shared.cpu
import com.github.seepick.kaml.k8s.shared.milliCpu
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.string.shouldContain

class PodTest : DescribeSpec({
    describe("simple test") {
        it("minimum") {
            Kaml.k8s.pod {
                container {
                    name = "my-container"
                    image = Image.nginx
                }
            }.toYaml() shouldBeEqual """
                apiVersion: v1
                kind: Pod
                metadata: {}
                spec:
                  containers:
                    - name: my-container
                      image: nginx
            """.trimIndent()
        }
        it("big") {
            Kaml.k8s.pod {
                metadata {
                    name = "my-podname"
                    namespace = "dev"
                    labels += "myLabel" to "myLabelValue"
                }
                restartPolicy = RestartPolicy.Never
                container {
                    name = "my-containername"
                    image = Image.Companion(name = "my-imagename", version = "my-imageVersion")
                    ports {
                        containerPort = 80
                        name = "my-portname"
                    }
                    readinessProbe {
                        httpGet(path = "/ready", port = 81)
                    }
                    livnessProbe {
                        httpGet(path = "/healthy", port = 82)
                    }
                    env {
                        values += "envKey" to "envVal"
                    }
                    resources {
                        requests {
                            cpu = 0.1.cpu
                            memory = 512.Mi
                        }
                        limits {
                            cpu = 500.milliCpu
                            memory = 1.Gi
                        }
                    }
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
                  restartPolicy: Never
                  containers:
                    - name: my-containername
                      image: my-imagename:my-imageVersion
                      ports:
                        - name: my-portname
                          containerPort: 80
                      readinessProbe:
                        httpGet:
                          path: /ready
                          port: 81
                      livenessProbe:
                        httpGet:
                          path: /healthy
                          port: 82
                      env:
                        - name: envKey
                          value: envVal
                      resources:
                        requests:
                          cpu: 0.1
                          memory: 512Mi
                        limits:
                          cpu: 500m
                          memory: 1Gi
            """.trimIndent()
        }

        // TODO implement env ref
//        it("env from configmap ref") {
//            Kaml.k8s.pod {
//                container {
//                    name = "anyContainer"
//                    image = Image.any()
//                    env += "MY_ENV_VAR" to fromConfigMap("my-configmap", "MY_ENV_VAR_KEY")
//                }
//            }.toYaml() shouldContain """
//                |      env:
//                |        - name: MY_ENV_VAR
//                |          valueFrom:
//                |            configMapKeyRef:
//                |              name: my-configmap
//                |              key: MY_ENV_VAR_KEY
//            """.trimMargin()
//        }
        it("env from configmap") {
            Kaml.k8s.pod {
                container {
                    name = "anyContainer"
                    image = Image.any()
                    env {
                        configMaps += "my-configmap"
                    }
                }
            }.toYaml() shouldContain """
                |      envFrom:
                |        - configMapRef:
                |            name: my-configmap
            """.trimMargin()
        }
// TODO security context
        /*
        spec: # or below container if want to be more specific for THIS container only
          securityContext:
            runAsUser: 1000
            capabilities:
              add: [ "MAC_ADMIN" ]
         */
// TODO read config map from volume:
        /*
        volumes:
        - name: app-config-volume
          configMap:
            name: app-config
         */
        it("env from secret") {
            Kaml.k8s.pod {
                container {
                    name = "anyContainer"
                    image = Image.any()
                    env {
                        secrets += "my-secret"
                    }
                }
            }.toYaml() shouldContain """
                |      envFrom:
                |        - secretRef:
                |            name: my-secret
            """.trimMargin()
        }
    }
    // TODO teint
    /*
    spec:
      containers: ...
      tolerations:
        - key: app
          operator: Equal
          value: blue
          effect: NoSchedule
     */
})