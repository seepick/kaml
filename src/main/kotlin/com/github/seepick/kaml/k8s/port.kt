package com.github.seepick.kaml.k8s

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.yaml.YamlMapDsl

data class Port(
    val name: String?,
    val containerPort: Int?, // NOT for NodePort type
    val protocol: Protocol?,
    val targetPort: Int?,
    val port: Int?,
    val nodePort: Int?,
)

@KamlDsl
class PortDsl {
    var name: String? = null
    var protocol: Protocol? = null
    /** for the service */
    var port: Int? = null
    /** for the node; valid range: 30000-32767; default: a random valid port */
    var nodePort: Int? = null
    // TODO could add some semantic checks here
    /** for the pod/container; defaults to 'port' value */
    var targetPort: Int? = null
    /** ? */
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
