package com.github.seepick.kaml.k8s.deployment

import com.github.seepick.kaml.ImageFormatter
import com.github.seepick.kaml.k8s.toYamlPattern
import com.github.seepick.kaml.yaml.yamlMap
import com.github.seepick.kaml.yaml.yamlSeq

fun Deployment.toYamlString() = toYamlPattern(
    buildSpec = { spec ->
        yamlMap().add("replicas", spec.replicas)
            .add(
                "selector",
                yamlMap().add(
                    "matchLabels", yamlMap()
                        .also {
                            spec.selector.matchLabels.forEach { (key, value) ->
                                it.add(key, value)
                            }
                        }
                        .build()).build()
            )
            .add(
                "template", yamlMap()
                    .add(
                        "metadata",
                        yamlMap().add(
                            "labels", yamlMap()
                                .also {
                                    spec.template.metadata.labels.forEach { (key, value) ->
                                        it.add(key, value)
                                    }
                                }
                                .build())
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
