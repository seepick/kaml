package com.github.seepick.kaml.k8s.service

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.Konfig
import com.github.seepick.kaml.k8s.K8s
import com.github.seepick.kaml.k8s.Metadata
import com.github.seepick.kaml.k8s.MetadataDsl
import com.github.seepick.kaml.k8s.Port
import com.github.seepick.kaml.k8s.PortDsl
import com.github.seepick.kaml.k8s.XK8s
import java.lang.constant.ConstantDescs.DEFAULT_NAME

fun K8s.service(konfig: Konfig = Konfig.default, code: ServiceDsl.() -> Unit): Service =
    ServiceDsl(konfig).apply(code).build()

fun XK8s.service(code: ServiceDsl.() -> Unit): Service =
    K8s.service(konfig, code)


@KamlDsl
class ServiceDsl(private val konfig: Konfig) {

    private var metadata = Metadata.default.copy(name = DEFAULT_NAME)
    fun metadata(code: MetadataDsl.() -> Unit) {
        metadata = MetadataDsl().also { it.name = DEFAULT_NAME }.apply(code).build()
    }

    var type = ServiceType.default

    private val ports = mutableListOf<Port>()
    fun ports(code: PortDsl.() -> Unit) {
        ports += PortDsl().apply(code).build()
    }

    /** Labels defined in the pod's (deployment template's) metadata. */
    val selector = mutableMapOf<String, String>()

    fun build() = Service(
        metadata = metadata,
        spec = ServiceSpec(
            type = type,
            ports = ports,
            selector = selector,
        )
    )
}
