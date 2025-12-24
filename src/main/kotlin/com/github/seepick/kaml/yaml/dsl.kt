package com.github.seepick.kaml.yaml

import com.amihaiemil.eoyaml.YamlMapping
import com.amihaiemil.eoyaml.YamlMappingBuilder
import com.amihaiemil.eoyaml.YamlNode
import com.amihaiemil.eoyaml.YamlSequence
import com.amihaiemil.eoyaml.YamlSequenceBuilder
import com.github.seepick.kaml.k8s.Manifest
import com.github.seepick.kaml.k8s.addManifest
import java.time.LocalDate
import java.time.LocalDateTime

object YamlRoot {
    fun <Spec> k8sManifest(manifest: Manifest<Spec>, spec: YamlMapDsl.() -> Unit) =
        map {
            addManifest(manifest, spec)
        }

    fun map(code: YamlMapDsl.() -> Unit) =
        YamlMapDsl().apply(code).build()

    fun list(code: YamlSeqDsl.() -> Unit) =
        YamlSeqDsl().apply(code).build()
}

@DslMarker
annotation class YamlDsl

@YamlDsl
class YamlSeqDsl {
    private val items = mutableListOf<Any>()

    fun add(value: Any) {
        items += value
    }

    fun flatMap(code: YamlMapDsl.() -> Unit) {
        add(YamlMapDsl().apply(code).build())
    }

    fun build(): YamlSequence =
        yamlSeq().addAllTypeSafe(items).build()
}

@YamlDsl
class YamlMapDsl {
    private val items = mutableMapOf<String, Any>()

    fun map(keyName: String, code: YamlMapDsl.() -> Unit) {
        items[keyName] = YamlMapDsl().apply(code).build()
    }

    fun seq(rootName: String, code: YamlSeqDsl.() -> Unit) {
        items[rootName] = YamlSeqDsl().apply(code).build()
    }

    fun addAll(subs: Map<String, String>) {
        subs.forEach {
            items[it.key] = it.value
        }
    }

    fun add(key: String, value: Any) {
        // could validate type here already...
        items[key] = value
    }

    fun addIfNotNull(key: String, value: Any?) {
        if (value != null) add(key, value)
    }

    fun build(): YamlMapping {
        return yamlMap().also { root ->
            items.forEach { (k, v) ->
                root.addTypeSafe(k, v)
            }
        }.build()
    }
}

fun YamlSequenceBuilder.addTypeSafe(value: Any) {
    when (value) {
        is String -> add(value)
        is Boolean -> add(value)
        is Int -> add(value)
        is Long -> add(value)
        is Double -> add(value)
        is Float -> add(value)
        is YamlNode -> add(value)
        is LocalDate -> add(value)
        is LocalDateTime -> add(value)
        else -> error("unsupported value type: ${value::class}")
    }
}

fun YamlMappingBuilder.addTypeSafe(key: String, value: Any) {
    when (value) {
        is String -> add(key, value)
        is Boolean -> add(key, value)
        is Int -> add(key, value)
        is Long -> add(key, value)
        is Double -> add(key, value)
        is Float -> add(key, value)
        is YamlNode -> add(key, value)
        is LocalDate -> add(key, value)
        is LocalDateTime -> add(key, value)
        else -> error("unsupported value type: ${value::class}")
    }
}

