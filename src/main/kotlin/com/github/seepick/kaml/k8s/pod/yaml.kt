package com.github.seepick.kaml.k8s.pod

import com.github.seepick.kaml.ImageFormatter
import com.github.seepick.kaml.k8s.toYamlPattern
import com.github.seepick.kaml.yamlMap
import com.github.seepick.kaml.yamlSeq

fun Pod.toYamlString() = toYamlPattern(
    buildSpec = { spec ->
        yamlMap().add("containers", yamlSeq().also { seq ->
                spec.containers.forEach { container ->
                    seq.add(
                        yamlMap().add("name", container.name)
                        .add("image", container.image.format(ImageFormatter.Docker)).also { portParent ->
                            if (container.ports.isNotEmpty()) portParent.add(
                                "ports", yamlSeq().also { ports ->
                                    container.ports.forEach { port ->
                                        ports.add(
                                            yamlMap().add("containerPort", port.containerPort).add("name", port.name)
                                                .build()
                                        )
                                    }
                                }.build()
                            )
                        }.build())
                }
            }.build()).build()
    })
