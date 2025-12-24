package com.github.seepick.kaml.k8s

import com.github.seepick.kaml.ImageFormatter
import com.github.seepick.kaml.yaml.YamlMapDsl

fun <Spec> YamlMapDsl.addManifest(manifest: Manifest<Spec>, spec: YamlMapDsl.() -> Unit) {
    add("apiVersion", manifest.apiVersion.yamlValue)
    add("kind", manifest.kind.yamlValue)
    addMetadata(manifest.metadata)
    map("spec") {
        spec()
    }
}

fun YamlMapDsl.addContainers(containers: List<Container>) {
    seq("containers") {
        containers.forEach { container ->
            flatMap {
                add("name", container.name)
                add("image", container.image.format(ImageFormatter.Docker))
                if (container.ports.isNotEmpty()) {
                    seq("ports") {
                        container.ports.forEach { port ->
                            flatMap {
                                add("containerPort", port.containerPort)
                                add("name", port.name)
                            }
                        }
                    }
                }
                if (container.env.isNotEmpty()) {
                    seq("env") {
                        container.env.forEach { (name, value) ->
                            flatMap {
                                add("name", name)
                                add("value", value)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun YamlMapDsl.addMetadata(metadata: Metadata) {
    map("metadata") {
        if (metadata.name != null) add("name", metadata.name)
        if (metadata.labels.isNotEmpty()) {
            map("labels") {
                addAll(metadata.labels)
            }
        }
    }
}
