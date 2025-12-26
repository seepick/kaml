package com.github.seepick.kaml.k8s.shared

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.yaml.YamlMapDsl

data class ContainerPort(
    val name: String?,
    val containerPort: Int?,
    val protocol: Protocol?,
)

@KamlDsl
class ContainerPortDsl {
    var name: String? = null
    var protocol: Protocol? = null
    /** when declaring a Pod, for a container */
    var containerPort: Int? = null

    internal fun build() = ContainerPort(
        name = name,
        protocol = protocol,
        containerPort = containerPort,
    )
}

fun YamlMapDsl.addContainerPorts(ports: List<ContainerPort>) {
    if (ports.isNotEmpty()) {
        seq("ports") {
            ports.forEach { port ->
                flatMap {
                    addIfNotNull("name", port.name)
                    addIfNotNull("containerPort", port.containerPort)
                    addIfNotNull("protocol", port.protocol?.yamlValue)
                }
            }
        }
    }
}
