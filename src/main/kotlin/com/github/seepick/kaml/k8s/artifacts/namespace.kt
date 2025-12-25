package com.github.seepick.kaml.k8s.artifacts

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.KamlException
import com.github.seepick.kaml.KamlYamlOutput
import com.github.seepick.kaml.Konfig
import com.github.seepick.kaml.k8s.K8s
import com.github.seepick.kaml.k8s.XK8s
import com.github.seepick.kaml.k8s.shared.K8sApiVersion
import com.github.seepick.kaml.k8s.shared.Manifest
import com.github.seepick.kaml.k8s.shared.ManifestKind
import com.github.seepick.kaml.k8s.shared.Metadata
import com.github.seepick.kaml.k8s.shared.MetadataDsl
import com.github.seepick.kaml.yaml.YamlRoot

fun K8s.namespace(konfig: Konfig = Konfig.default, code: NamespaceDsl.() -> Unit): Namespace =
    NamespaceDsl(konfig).apply(code).build()

fun XK8s.namespace(code: NamespaceDsl.() -> Unit): Namespace =
    K8s.namespace(konfig, code)


@KamlDsl
class NamespaceDsl(private val konfig: Konfig) {

    protected var _metadata: Metadata = Metadata.Companion.default
        private set

    fun metadata(code: MetadataDsl.() -> Unit) {
        _metadata = MetadataDsl().apply(code).build()
    }

    internal fun build() = Namespace(
        metadata = _metadata
    ).also {
        if (it.metadata.name.isNullOrEmpty()) {
            // always check and throw, independent of validation mode
            throw KamlException("namespace name must be set")
        }
    }
}

data class Namespace(
    override val metadata: Metadata
) : Manifest<Any?>, KamlYamlOutput {
    override val apiVersion = K8sApiVersion.Namespace
    override val kind = ManifestKind.Namespace
    override val spec: Any? = null

    override fun toYamlNode() =
        YamlRoot.k8sManifest(this, skipSpec = true) {}
    // FIXME validateable for name set
}
