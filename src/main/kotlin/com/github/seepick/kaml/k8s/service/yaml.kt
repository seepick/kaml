package com.github.seepick.kaml.k8s.service

import com.github.seepick.kaml.k8s.addPorts
import com.github.seepick.kaml.yaml.YamlRoot
import com.github.seepick.kaml.yaml.toCleanYamlString

fun Service.toYamlString() = YamlRoot.k8sManifest(this) {
    add("type", spec.type.yamlValue)
    addPorts(spec.ports)
    map("selector") {
        addAll(spec.selector)
    }
}.toCleanYamlString()
