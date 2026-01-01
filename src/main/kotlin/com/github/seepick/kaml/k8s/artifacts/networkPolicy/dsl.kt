package com.github.seepick.kaml.k8s.artifacts.networkPolicy

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.KamlKonfig
import com.github.seepick.kaml.k8s.K8s
import com.github.seepick.kaml.k8s.XK8s
import com.github.seepick.kaml.k8s.shared.Metadata
import com.github.seepick.kaml.k8s.shared.MetadataDsl
import com.github.seepick.kaml.k8s.shared.Protocol
import com.github.seepick.kaml.k8s.shared.Selector
import com.github.seepick.kaml.k8s.shared.SelectorDsl
import com.github.seepick.kaml.k8s.shared.SimplePort
import com.github.seepick.kaml.validation.DomainBuilder
import com.github.seepick.kaml.validation.buildValidated
import java.lang.constant.ConstantDescs.DEFAULT_NAME


fun K8s.networkPolicy(konfig: KamlKonfig = KamlKonfig.default, code: NetworkPolicyDsl.() -> Unit) =
    NetworkPolicyDsl().apply(code).buildValidated(konfig)

fun XK8s.networkPolicy(code: NetworkPolicyDsl.() -> Unit) =
    K8s.networkPolicy(konfig, code)

class MetadataReuseImpl : MetadataReuse {

    override var name: String?
        get() = metadata.name
        set(value) {
            metadata = metadata.copy(name = value)
        }

    internal var metadata = Metadata.default.copy(name = DEFAULT_NAME)
        private set

    override fun metadata(code: MetadataDsl.() -> Unit) {
        metadata = MetadataDsl().also { it.name = DEFAULT_NAME }.apply(code).build()
    }
}

interface MetadataReuse {
    /** A shortcut for `metadata.name`. */
    var name: String?

    fun metadata(code: MetadataDsl.() -> Unit)
}

@KamlDsl
class NetworkPolicyDsl(
    private val metaReuse: MetadataReuseImpl = MetadataReuseImpl()
) : DomainBuilder<NetworkPolicy>, MetadataReuse by metaReuse {

    private var podSelector = Selector.default
    fun podSelector(code: SelectorDsl.() -> Unit) {
        podSelector = SelectorDsl().apply(code).build()
    }

    private val ingresses = mutableListOf<Ingress>()
    fun ingress(code: IngressDsl.() -> Unit) {
        ingresses += IngressDsl().apply(code).build()
    }

    // TODO support egress

    override fun build() = NetworkPolicy(
        metadata = metaReuse.metadata,
        spec = NetworkPolicySpec(
            podSelector = podSelector,
            ingresses = ingresses,
        )
    )
}

data class IpBlock(val cidr: String)

@KamlDsl
class RuleDsl {

    private var podSelector: Selector? = null
    fun podSelector(code: SelectorDsl.() -> Unit) {
        podSelector = SelectorDsl().apply(code).build()
    }

    private var namespaceSelector: Selector? = null
    fun namespaceSelector(code: SelectorDsl.() -> Unit) {
        namespaceSelector = SelectorDsl().apply(code).build()
    }

    private var ipBlock: IpBlock? = null
    fun ipblock(cidr: String) {
        ipBlock = IpBlock(cidr)
    }

    internal fun build() = Rule(podSelector, namespaceSelector, ipBlock)
}

data class Rule(
    val podSelector: Selector?,
    val namespaceSelector: Selector?,
    val ipBlock: IpBlock?,
) // TODO : Validatable

@KamlDsl
class IngressDsl {

    private val fromRules = mutableListOf<Rule>()
    /** Add another incoming rule (in disjunctive form). */
    fun from(code: RuleDsl.() -> Unit) {
        fromRules += RuleDsl().apply(code).build()
    }

    private val ports = mutableListOf<SimplePort>()
    fun port(port: Int, protocol: Protocol) {
        ports += SimplePort(value = port, protocol = protocol)
    }

    fun build() = Ingress(
        ports = ports,
        fromRules = fromRules,
    )
}
