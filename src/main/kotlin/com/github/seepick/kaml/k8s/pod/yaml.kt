package com.github.seepick.kaml.k8s.pod

import com.github.seepick.kaml.ImageFormatter
import com.github.seepick.kaml.k8s.toYamlPattern
import com.github.seepick.kaml.yamlMap
import com.github.seepick.kaml.yamlSeq

fun Pod.toYamlString() = toYamlPattern(
    buildSpec = { spec ->
        yamlMap().add(
            "containers", yamlSeq()
                .also { seq ->
                    spec.containers.forEach { container ->
                        seq.add(
                            yamlMap()
                                .add("name", container.name)
                                .add("image", container.image.format(ImageFormatter.Docker))
                                .build()
                        )
                    }
                }
                .build()).build()
    }
)
