package com.github.seepick.kaml.k8s.pod

import com.github.seepick.kaml.k8s.addContainers
import com.github.seepick.kaml.yaml.YamlRoot
import com.github.seepick.kaml.yaml.toCleanYamlString

fun Pod.toYamlString() = YamlRoot.k8sManifest(this) {
    addContainers(spec.containers)

}.toCleanYamlString()
