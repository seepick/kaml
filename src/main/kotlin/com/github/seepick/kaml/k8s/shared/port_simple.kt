package com.github.seepick.kaml.k8s.shared

import com.github.seepick.kaml.validation.Validatable
import com.github.seepick.kaml.validation.validation
import com.github.seepick.kaml.yaml.YamlMapDsl

data class SimplePort(
    val protocol: Protocol,
    val value: Int,
) : Validatable {
    override fun validate() = validation {
        valid(value >= 0) { "Port must be positive but was: $value" }
    }
}

fun YamlMapDsl.addSimplePorts(name: String = "ports", ports: List<SimplePort>) {
    if (ports.isNotEmpty()) {
        seq(name) {
            ports.forEach { port ->
                flatMap {
                    add("protocol", port.protocol.yamlValue)
                    add("port", port.value)
                }
            }
        }
    }
}
