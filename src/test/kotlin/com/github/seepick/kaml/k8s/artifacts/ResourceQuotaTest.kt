package com.github.seepick.kaml.k8s.artifacts

import com.github.seepick.kaml.Kaml
import com.github.seepick.kaml.k8s.k8s
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equals.shouldBeEqual

class ResourceQuotaTest : StringSpec({
    "empty" {
        Kaml.k8s.resourceQuota {
            metadata {
                name = "my-quota"
            }
            hard {
                // FIXME implement me
                pods = 10
                requestsCpu = "1"
                limitsCpu = "2"
                requestsMemory = "5Gi"
                limitsMemory = "10Gi"
            }
        }.toYaml() shouldBeEqual """
            apiVersion: v1
            kind: ResourceQuota
            metadata:
              name: my-quota
        """.trimIndent()
    }
})