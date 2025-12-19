package com.github.seepick.kaml.k8s.deployment

import com.github.seepick.kaml.ImageFormatter
import com.github.seepick.kaml.k8s.toYamlPattern
import com.github.seepick.kaml.yamlMap
import com.github.seepick.kaml.yamlSeq

fun K8sDeployment.toYamlString() = toYamlPattern(
    buildMetadata = { metadata ->
        yamlMap().add("name", metadata.name).build()
    },
    buildSpec = { spec ->
        yamlMap().add("replicas", spec.replicas)
            .add(
                "selector",
                yamlMap().add("matchLabels", yamlMap().add("app", spec.selector.matchLabelsApp).build()).build()
            )
            .add(
                "template", yamlMap()
                    .add(
                        "metadata",
                        yamlMap().add("labels", yamlMap().add("app", spec.template.metadataLabelsApp).build())
                            .build()
                    )
                    .add("spec", yamlMap().add("containers", yamlSeq().also { seq ->
                        spec.template.containers.forEach { container ->
                            seq.add(
                                yamlMap()
                                    .add("name", container.name)
                                    .add("image", container.image.format(ImageFormatter.Docker))
                                    .build()
                            )
                        }
                    }.build()).build())
                    .build()
            )
            .build()
    },
)
