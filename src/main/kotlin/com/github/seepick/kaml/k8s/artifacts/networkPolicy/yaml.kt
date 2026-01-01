package com.github.seepick.kaml.k8s.artifacts.networkPolicy

import com.github.seepick.kaml.k8s.shared.Selector
import com.github.seepick.kaml.k8s.shared.addSimplePorts
import com.github.seepick.kaml.yaml.YamlMapDsl
import com.github.seepick.kaml.yaml.YamlRoot

internal fun NetworkPolicy._toYamlNode() = YamlRoot.k8sManifest(this) {
    map("podSelector") {
        map("matchLabels") {
            addAll(spec.podSelector.matchLabels)
        }
    }
    seq("policyTypes") {
        if (spec.ingresses.isNotEmpty()) {
            add(PolicyType.Ingress.yamlValue)
        }
        // same for egresses
    }
    if (spec.ingresses.isNotEmpty()) {
        seq("ingress") {
            spec.ingresses.forEach { ingress ->
                flatMap {
                    seq("from") {
                        ingress.fromRules.forEach { rule ->
                            flatMap {
                                addSelector(rule.podSelector, "podSelector", skipIfEmpty = true)
                                addSelector(rule.namespaceSelector, "namespaceSelector", skipIfEmpty = true)
                                if (rule.ipBlock != null) {
                                    map("ipBlock") {
                                        add("cidr", rule.ipBlock.cidr)
                                    }
                                }
                            }
                        }
                    }
                    addSimplePorts("ports", ingress.ports)
                }
            }
        }
    }
}

fun YamlMapDsl.addSelector(selector: Selector?, name: String, skipIfEmpty: Boolean = false) {
    if (selector == null || (selector.isEmpty() && skipIfEmpty)) return
    map(name) {
        map("matchLabels") {
            addAll(selector.matchLabels)
        }
    }
}
