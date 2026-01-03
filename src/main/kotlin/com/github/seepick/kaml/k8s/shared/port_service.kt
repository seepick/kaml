package com.github.seepick.kaml.k8s.shared

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.validation.Validatable
import com.github.seepick.kaml.validation.invalid
import com.github.seepick.kaml.validation.validation
import com.github.seepick.kaml.yaml.YamlMapDsl

data class ServicePort(
//    appProtocol   <string>
    val name: String?,
    val protocol: Protocol?,

    val nodePort: Int?,
    val port: Int,
    val targetPort: Int?, // IntOrString
) : Validatable {
    companion object {
        const val NODE_PORT_MIN = 30_000
        const val NODE_PORT_MAX = 32_767
    }

    override fun validate() = validation {
        if (nodePort != null) {
            valid(nodePort in NODE_PORT_MIN..NODE_PORT_MAX) {
                "Node port must be between $NODE_PORT_MIN and $NODE_PORT_MAX but was: $nodePort"
            }
        }
    }
}

@KamlDsl
class ServicePortDsl {
    var name: String? = null
    var protocol: Protocol? = null
    /** for the node itself; valid range: 30000-32767; defaults to a random valid port */
    var nodePort: Int? = null
    /** for the service itself, the only mandatory field */
    var port: Int? = null
    // TODO could add some semantic checks here
    /** where the service forwards requests to; for the pod; defaults to 'port' value */
    var targetPort: Int? = null // TODO can also be a name (deploy.spec.template.port.name)

    internal fun build() = ServicePort(
        name = name,
        protocol = protocol,
        port = port ?: invalid("port must be set for service port"),
        nodePort = nodePort,
        targetPort = targetPort ?: port,
    )
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
