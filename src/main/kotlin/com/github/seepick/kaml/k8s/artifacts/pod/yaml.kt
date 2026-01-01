package com.github.seepick.kaml.k8s.artifacts.pod

import com.github.seepick.kaml.k8s.shared.addContainers
import com.github.seepick.kaml.yaml.YamlMapDsl
import com.github.seepick.kaml.yaml.YamlRoot

internal fun Pod._toYamlNode() = YamlRoot.k8sManifest(this) {
    addPodSpec(spec)
}

internal fun YamlMapDsl.addPodSpec(spec: PodSpec) {
    if (spec.restartPolicy != null) {
        add("restartPolicy", spec.restartPolicy.yamlValue)
    }
    addContainers(spec.containers)
    if (spec.volumes.isNotEmpty()) {
        seq("volumes") {
            spec.volumes.forEach { volume ->
                flatMap {
                    add("name", volume.name)
                    addIfNotNull("mountPath", volume.mountPath)
                }
            }
        }
    }
    if (spec.hostPath != null) {
        map("hostPath") {
            add("path", spec.hostPath.path)
            add("type", spec.hostPath.type.yamlValue)
        }
    }
}
