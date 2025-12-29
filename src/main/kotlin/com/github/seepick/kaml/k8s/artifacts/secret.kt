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

fun K8s.secret(konfig: KamlKonfig = KamlKonfig.default, code: SecretDsl.() -> Unit) =
    SecretDsl(konfig).apply(code).build()

fun XK8s.secret(code: SecretDsl.() -> Unit) =
    K8s.secret(konfig, code)

// TODO merge together with ConfigMap
// TODO prevent keys from printed via toString

@KamlDsl
class SecretDsl(private val konfig: KamlKonfig) {

    protected var _metadata: Metadata = Metadata.Companion.default
        private set

    val data = mutableMapOf<String, String>()

    fun metadata(code: MetadataDsl.() -> Unit) {
        _metadata = MetadataDsl().apply(code).build()
    }

    internal fun build() = Secret(
        metadata = _metadata,
        data = data,
    )
}

data class Secret(
    override val metadata: Metadata,
    val data: Map<String, String>,
) : Manifest<Any?>, KamlYamlOutput {
    override val apiVersion = K8sApiVersion.Secret
    override val kind = ManifestKind.Secret
    override val spec: Any? = null

    override fun toYamlNode() =
        YamlRoot.k8sManifest(this, skipSpec = true, spec = skipSpec, additional = {
            map("data") {
                data.forEach { (key, value) -> add(key, value) }
            }
        })
    // TODO validate: all values MUST be base64 encoded!
}
