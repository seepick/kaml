package com.github.seepick.kaml.k8s.shared

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.yaml.YamlMapDsl

data class Port(
    val name: String?,
    val containerPort: Int?,
    val protocol: Protocol?,
)

data class ServicePort(
    val name: String?,
    val protocol: Protocol?,

    val nodePort: Int?,
    val port: Int?,
    val targetPort: Int?,
)

@KamlDsl
class ServicePortDsl {
    var name: String? = null
    var protocol: Protocol? = null
    /** for the node itself; valid range: 30000-32767; defaults to a random valid port */
    var nodePort: Int? = null
    /** for the service itself */
    var port: Int? = null
    // TODO could add some semantic checks here
    /** where the service forwards requests to; for the pod; defaults to 'port' value */
    var targetPort: Int? = null

    internal fun build() = ServicePort(
        name = name,
        protocol = protocol,
        port = port,
        nodePort = nodePort,
        targetPort = targetPort ?: port,
    )
}
@KamlDsl
class PortDsl {
    var name: String? = null
    var protocol: Protocol? = null
    /** when declaring a Pod, for a container */
    var containerPort: Int? = null

    internal fun build() = Port(
        name = name,
        protocol = protocol,
        containerPort = containerPort,
    )
}

fun YamlMapDsl.addPorts(ports: List<Port>) {
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

fun YamlMapDsl.addServicePorts(ports: List<ServicePort>) {
    if (ports.isNotEmpty()) {
        seq("ports") {
            ports.forEach { port ->
                flatMap {
                    addIfNotNull("name", port.name)
                    addIfNotNull("protocol", port.protocol?.yamlValue)
                    addIfNotNull("nodePort", port.nodePort)
                    addIfNotNull("port", port.port)
                    addIfNotNull("targetPort", port.targetPort)
                }
            }
        }
    }
}
