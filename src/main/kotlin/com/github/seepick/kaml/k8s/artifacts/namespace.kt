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
import com.github.seepick.kaml.validation.Validatable
import com.github.seepick.kaml.validation.handleValidation
import com.github.seepick.kaml.validation.validation
import com.github.seepick.kaml.yaml.YamlRoot

fun K8s.namespace(konfig: KamlKonfig = KamlKonfig.default, code: NamespaceDsl.() -> Unit): Namespace =
    NamespaceDsl(konfig).apply(code).build()

fun XK8s.namespace(code: NamespaceDsl.() -> Unit): Namespace =
    K8s.namespace(konfig, code)


@KamlDsl
class NamespaceDsl(private val konfig: KamlKonfig) {

    protected var _metadata: Metadata = Metadata.Companion.default
        private set

    fun metadata(code: MetadataDsl.() -> Unit) {
        _metadata = MetadataDsl().apply(code).build()
    }

    internal fun build() = handleValidation(konfig, Namespace(_metadata))
}

data class Namespace(
    override val metadata: Metadata
) : Manifest<Any?>, KamlYamlOutput, Validatable {
    override val apiVersion = K8sApiVersion.Namespace
    override val kind = ManifestKind.Namespace
    override val spec: Any? = null

    override fun toYamlNode() =
        YamlRoot.k8sManifest(this, skipSpec = true) {}

    override fun validate() = validation {
        valid(!metadata.name.isNullOrEmpty()) { "Namespace name must not be null or empty!" }
    }
}
