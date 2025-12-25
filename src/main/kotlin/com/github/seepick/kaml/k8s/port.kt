package com.github.seepick.kaml.k8s

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.yaml.YamlMapDsl

data class Port(
    val name: String?,
    val containerPort: Int?, // NOT for NodePort type
    val protocol: Protocol?,

    // TODO could add some semantic checks here
    /** for the pod/container */
    val targetPort: Int?,
    /** for the service */
    val port: Int?,
    /** for the node; valid range: 30000-32767 */
    val nodePort: Int?,
)

@KamlDsl
class PortDsl {
    var name: String? = null
    var protocol: Protocol? = null
    var port: Int? = null
    var nodePort: Int? = null
    var targetPort: Int? = null
    var containerPort: Int? = null

    internal fun build() = Port(
        name = name,
        protocol = protocol,
        port = port,
        nodePort = nodePort,
        targetPort = targetPort,
        containerPort = containerPort,
    )
}

fun YamlMapDsl.addPorts(ports: List<Port>) {
    if (ports.isNotEmpty()) {
        seq("ports") {
            ports.forEach { port ->
                flatMap {
                    addIfNotNull("name", port.name)
                    addIfNotNull("targetPort", port.targetPort)
                    addIfNotNull("port", port.port)
                    addIfNotNull("containerPort", port.containerPort)
                    addIfNotNull("protocol", port.protocol?.yamlValue)
                    addIfNotNull("nodePort", port.nodePort)
                }
            }
        }
    }
}
