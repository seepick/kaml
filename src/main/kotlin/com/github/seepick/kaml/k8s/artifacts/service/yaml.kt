package com.github.seepick.kaml.k8s.artifacts.service

import com.github.seepick.kaml.k8s.shared.addServicePorts
import com.github.seepick.kaml.yaml.YamlRoot

internal fun Service._toYamlNode() = YamlRoot.k8sManifest(this) {
    add("type", spec.type.yamlValue)
    addServicePorts(spec.ports)
    map("selector") {
        addAll(spec.selector)
    }
}
