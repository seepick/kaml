package com.github.seepick.kaml.k8s.deployment

import com.github.seepick.kaml.k8s.addContainers
import com.github.seepick.kaml.k8s.addMetadata
import com.github.seepick.kaml.yaml.YamlRoot
import com.github.seepick.kaml.yaml.toCleanYamlString

fun Deployment.toYamlString() = YamlRoot.k8sManifest(this) {
    add("replicas", spec.replicas)
    map("selector") {
        map("matchLabels") {
            addAll(spec.selector.matchLabels)
        }
    }
    map("template") {
        addMetadata(spec.template.metadata)
        map("spec") {
            addContainers(spec.template.containers)
        }
    }
}.toCleanYamlString()
