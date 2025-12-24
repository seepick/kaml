package com.github.seepick.kaml.k8s.service

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.k8s.K8s
import com.github.seepick.kaml.k8s.Metadata
import com.github.seepick.kaml.k8s.MetadataDsl
import com.github.seepick.kaml.k8s.Port
import com.github.seepick.kaml.k8s.PortDsl
import java.lang.constant.ConstantDescs.DEFAULT_NAME

fun K8s.service(code: ServiceDsl.() -> Unit): Service =
    ServiceDsl().apply(code).build()

@KamlDsl
class ServiceDsl {

    private var metadata = Metadata.default.copy(name = DEFAULT_NAME)
    fun metadata(code: MetadataDsl.() -> Unit) {
        metadata = MetadataDsl().also { it.name = DEFAULT_NAME }.apply(code).build()
    }

    var serviceType = ServiceType.default

    private val ports = mutableListOf<Port>()
    fun ports(code: PortDsl.() -> Unit) {
        ports += PortDsl().apply(code).build()
    }

    val selector = mutableMapOf<String, String>()

    fun build() = Service(
        metadata = metadata,
        spec = ServiceSpec(
            type = serviceType,
            ports = ports,
            selector = selector,
        )
    )
}
