package com.github.seepick.kaml.k8s.artifacts.service

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.KamlKonfig
import com.github.seepick.kaml.k8s.K8s
import com.github.seepick.kaml.k8s.XK8s
import com.github.seepick.kaml.k8s.shared.Metadata
import com.github.seepick.kaml.k8s.shared.MetadataDsl
import com.github.seepick.kaml.k8s.shared.ServicePort
import com.github.seepick.kaml.k8s.shared.ServicePortDsl
import com.github.seepick.kaml.validation.handleValidation
import java.lang.constant.ConstantDescs.DEFAULT_NAME

fun K8s.service(konfig: KamlKonfig = KamlKonfig.default, code: ServiceDsl.() -> Unit) =
    ServiceDsl(konfig).apply(code).build()

fun XK8s.service(code: ServiceDsl.() -> Unit) =
    K8s.service(konfig, code)


@KamlDsl
class ServiceDsl(private val konfig: KamlKonfig) {

    private var metadata = Metadata.default.copy(name = DEFAULT_NAME)
    fun metadata(code: MetadataDsl.() -> Unit) {
        metadata = MetadataDsl().also { it.name = DEFAULT_NAME }.apply(code).build()
    }

    var type = ServiceType.default

    private val ports = mutableListOf<ServicePort>()
    fun ports(code: ServicePortDsl.() -> Unit) {
        ports += ServicePortDsl().apply(code).build()
    }

    /** Labels defined in the pod's (deployment template's) metadata. */
    val selector = mutableMapOf<String, String>()

    fun build() = handleValidation(
        konfig, Service(
            metadata = metadata,
            spec = ServiceSpec(
                type = type,
                ports = ports,
                selector = selector,
            )
        )
    )
}
