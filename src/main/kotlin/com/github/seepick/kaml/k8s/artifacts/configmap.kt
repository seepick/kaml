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
import com.github.seepick.kaml.yaml.YamlRoot
import com.github.seepick.kaml.yaml.skipSpec

fun K8s.configMap(konfig: KamlKonfig = KamlKonfig.default, code: ConfigMapDsl.() -> Unit) =
    ConfigMapDsl(konfig).apply(code).build()

fun XK8s.configMap(code: ConfigMapDsl.() -> Unit) =
    K8s.configMap(konfig, code)

@KamlDsl
class ConfigMapDsl(private val konfig: KamlKonfig) {

    protected var _metadata: Metadata = Metadata.Companion.default
        private set

    val data = mutableMapOf<String, String>()

    fun metadata(code: MetadataDsl.() -> Unit) {
        _metadata = MetadataDsl().apply(code).build()
    }

    internal fun build() = ConfigMap(
        metadata = _metadata,
        data = data,
    )
}

data class ConfigMap(
    override val metadata: Metadata,
    val data: Map<String, String>,
) : Manifest<Any?>, KamlYamlOutput {
    override val apiVersion = K8sApiVersion.ConfigMap
    override val kind = ManifestKind.ConfigMap
    override val spec: Any? = null

    override fun toYamlNode() =
        YamlRoot.k8sManifest(this, skipSpec = true, spec = skipSpec, additional = {
            map("data") {
                data.forEach { (key, value) -> add(key, value) }
            }
        })
}
