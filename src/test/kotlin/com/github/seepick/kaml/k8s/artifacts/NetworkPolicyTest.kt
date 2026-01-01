package com.github.seepick.kaml.k8s.artifacts

import com.github.seepick.kaml.Kaml
import com.github.seepick.kaml.k8s.artifacts.networkPolicy.networkPolicy
import com.github.seepick.kaml.k8s.k8s
import com.github.seepick.kaml.k8s.shared.Protocol
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equals.shouldBeEqual

class NetworkPolicyTest : StringSpec({
    "sunshine case" {
        Kaml.k8s.networkPolicy {
            name = "test-network-policy"
            podSelector {
                matchLabels += "role" to "db"
            }
            ingress {
                from {
                    podSelector {
                        matchLabels += "role" to "api"
                    }
                    namespaceSelector {
                        matchLabels += "env" to "prod"
                    }
                }
                from {
                    ipblock(cidr = "192.168.0.0/16")
                }
                port(3306, Protocol.TCP)
            }
        }.toYaml() shouldBeEqual """
            apiVersion: networking.k8s.io/v1
            kind: NetworkPolicy
            metadata:
              name: test-network-policy
            spec:
              podSelector:
                matchLabels:
                  role: db
              policyTypes:
                - Ingress
              ingress:
                - from:
                    - podSelector:
                        matchLabels:
                          role: api
                      namespaceSelector:
                        matchLabels:
                          env: prod
                    - ipBlock:
                        cidr: 192.168.0.0/16
                  ports:
                    - protocol: TCP
                      port: 3306
        """.trimIndent()
    }
    /*
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: internal-policy
  namespace: default
spec:
  podSelector:
    matchLabels:
      name: internal
  policyTypes:
  - Egress
  - Ingress
  ingress:
  // # allow all incoming ;)
    - {}
  egress:
  - to:
    - podSelector:
        matchLabels:
          name: mysql
    ports:
    - protocol: TCP
      port: 3306

  - to:
    - podSelector:
        matchLabels:
          name: payroll
    ports:
    - protocol: TCP
      port: 8080
// # Any destination on UDP/TCP port 53 (for DNS resolution, required for service discovery in Kubernetes)
// # DNS Access: DNS is handled by the kube-dns service, which listens on port 53 for both UDP and TCP:
  - ports:
    - port: 53
      protocol: UDP
    - port: 53
      protocol: TCP
     */
})
