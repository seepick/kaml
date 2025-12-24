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

fun YamlMapDsl.addPorts(ports: List<Port>) {
    if (ports.isNotEmpty()) {
        seq("ports") {
            ports.forEach { port ->
                flatMap {
                    addIfNotNull("containerPort", port.containerPort)
                    addIfNotNull("name", port.name)
                    addIfNotNull("protocol", port.protocol?.yamlValue)
                    addIfNotNull("port", port.port)
                    addIfNotNull("targetPort", port.targetPort)
                    addIfNotNull("nodePort", port.nodePort)
                }
            }
        }
    }
}

fun YamlMapDsl.addEnv(env: Map<String, Any>) {
    if (env.isNotEmpty()) {
        seq("env") {
            env.forEach { (name, value) ->
                flatMap {
                    add("name", name)
                    add("value", value)
                }
            }
        }
    }
}

fun YamlMapDsl.addContainers(containers: List<Container>) {
    seq("containers") {
        containers.forEach { container ->
            flatMap {
                add("name", container.name)
                add("image", container.image.format(ImageFormatter.Docker))
                addPorts(container.ports)
                addEnv(container.env)
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
