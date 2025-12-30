package com.github.seepick.kaml.k8s.artifacts

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.KamlKonfig
import com.github.seepick.kaml.KamlYamlOutput
import com.github.seepick.kaml.k8s.K8s
import com.github.seepick.kaml.k8s.XK8s
import com.github.seepick.kaml.k8s.shared.K8sApiVersion
import com.github.seepick.kaml.k8s.shared.Manifest
import com.github.seepick.kaml.k8s.shared.ManifestKind
import com.github.seepick.kaml.k8s.shared.Metadata
import com.github.seepick.kaml.k8s.shared.MetadataDsl
import com.github.seepick.kaml.kerror
import com.github.seepick.kaml.yaml.YamlRoot

fun K8s.resourceQuota(konfig: KamlKonfig = KamlKonfig.default, code: ResourceQuotaDsl.() -> Unit) =
    ResourceQuotaDsl(konfig).apply(code).build()

fun XK8s.resourceQuota(code: ResourceQuotaDsl.() -> Unit) =
    K8s.resourceQuota(konfig, code)

@KamlDsl
class ResourceQuotaDsl(private val konfig: KamlKonfig) {

    protected var _metadata: Metadata = Metadata.Companion.default
        private set

    private var hardSpec: HardRQSpec? = null

    fun metadata(code: MetadataDsl.() -> Unit) {
        _metadata = MetadataDsl().apply(code).build()
    }

    fun hard(code: HardRQDsl.() -> Unit) {
        hardSpec = HardRQDsl().apply(code).build()
    }

    internal fun build() = ResourceQuota(
        metadata = _metadata,
        spec = ResourceQuotaSpec(
            hard = hardSpec ?: kerror("hard spec not set for resource quota ${_metadata.name}")
        )
    )
}

@KamlDsl
class HardRQDsl {
    var pods: Int? = null
    var requestsCpu: String? = null
    var requestsMemory: String? = null
    var limitsCpu: String? = null
    var limitsMemory: String? = null

    internal fun build() = HardRQSpec(
        pods = pods,
        requestsCpu = requestsCpu,
        requestsMemory = requestsMemory,
        limitsCpu = limitsCpu,
        limitsMemory = limitsMemory,
    )
}

data class ResourceQuota(
    override val metadata: Metadata,
    override val spec: ResourceQuotaSpec,
) : Manifest<ResourceQuotaSpec>, KamlYamlOutput {

    override val apiVersion = K8sApiVersion.ResourceQuota
    override val kind = ManifestKind.ResourceQuota

    override fun toYamlNode() = YamlRoot.k8sManifest(this, skipSpec = true) {
        map("hard") {
            addIfNotNull("pods", spec.hard.pods)
            addIfNotNull("requests.cpu", spec.hard.requestsCpu)
            addIfNotNull("requests.memory", spec.hard.requestsMemory)
            addIfNotNull("limits.cpu", spec.hard.limitsCpu)
            addIfNotNull("limits.memory", spec.hard.limitsMemory)
        }
    }
}

data class ResourceQuotaSpec(
    val hard: HardRQSpec
)

data class HardRQSpec(
    val pods: Int? = null,
    /** renders as "requests.cpu"; e.g. "4" */
    val requestsCpu: String? = null,
    /** e.g. "5Gi" */
    val requestsMemory: String? = null,
    /** e.g. "10" */
    val limitsCpu: String? = null,
    /** e.g. "10Gi" */
    val limitsMemory: String? = null,
)
