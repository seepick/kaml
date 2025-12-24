package com.github.seepick.kaml.k8s.pod

import com.github.seepick.kaml.ImageFormatter
import com.github.seepick.kaml.yaml.YamlRoot
import com.github.seepick.kaml.yaml.toCleanYamlString

fun Pod.toYamlString() = YamlRoot.map {
    add("apiVersion", apiVersion)
    add("kind", kind.yamlValue)
    map("metadata") {
        add("name", metadata.name)
        if (metadata.labels.isNotEmpty()) {
            map("labels") {
                addAll(metadata.labels)
            }
        }
    }
    map("spec") {
        seq("containers") {
            spec.containers.forEach { container ->
                flatMap {
                    add("name", container.name)
                    add("image", container.image.format(ImageFormatter.Docker))
                    seq("ports") {
                        container.ports.forEach { port ->
                            flatMap {
                                add("containerPort", port.containerPort)
                                add("name", port.name)
                            }
                        }
                    }
                }
            }
        }
    }


}.toCleanYamlString()
