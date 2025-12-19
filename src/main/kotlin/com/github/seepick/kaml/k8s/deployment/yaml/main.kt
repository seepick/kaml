package com.github.seepick.kaml.k8s.deployment.yaml

import com.github.seepick.kaml.ImageFormatter
import com.github.seepick.kaml.fixDashPlacement
import com.github.seepick.kaml.github.domain.GithubAction
import com.github.seepick.kaml.k8s.deployment.domain.K8sDeployment
import com.github.seepick.kaml.yamlMap
import com.github.seepick.kaml.yamlSeq
import sun.security.jca.ProviderList.add

fun K8sDeployment.toYamlString(): String {
    val root = yamlMap()
    root.add("apiVersion", "apps/v1")
    root.add("kind", "Deployment")
    root.add("metadata", yamlMap().add("name", name).build())
    val spec = yamlMap()
        .add("replicas", replicas)
        .add("selector", yamlMap().add("matchLabels", yamlMap().add("app", selector.matchLabelsApp).build()).build())
        .add(
            "template", yamlMap()
                .add(
                    "metadata",
                    yamlMap().add("labels", yamlMap().add("app", template.metadataLabelsApp).build()).build()
                )
                .add("spec", yamlMap().add("containers", yamlSeq().also { seq ->
                    template.containers.forEach { container ->
                        seq.add(
                            yamlMap().add("name", container.name).add("image", container.image.format(ImageFormatter.Docker)).build()
                        )
                    }
                }.build()).build())
                .build()
        )
    root.add("spec", spec.build())

//            spec:
//              replicas: 2
//              selector:
//                matchLabels:
//                  app: my-container
//              template:
//                metadata:
//                  labels:
//                    app: my-container
//                spec:
//                  containers:
//                    - name: my-container
//                      image: my-image:latest
    return root.build().toString()
        .let(::fixDashPlacement) // steps mapping-sequence with "-" prefixed
}