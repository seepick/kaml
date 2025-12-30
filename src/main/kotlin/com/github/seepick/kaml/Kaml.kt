package com.github.seepick.kaml

import com.amihaiemil.eoyaml.YamlNode
import com.github.seepick.kaml.validation.ValidationLevel
import com.github.seepick.kaml.yaml.toCleanYamlString
import java.io.File

object Kaml // extension functions
class XKaml(val konfig: KamlKonfig = KamlKonfig.default)

interface KamlYamlOutput {
    fun toYamlNode(): YamlNode
    fun toYaml(): String =
        toYamlNode().toCleanYamlString()
}

@DslMarker
annotation class KamlDsl

infix fun KamlYamlOutput.saveYamlTo(target: File) {
    target.writeText(toYaml())
}

open class KamlException internal constructor(message: String) : RuntimeException(message)

internal fun kerror(message: String): Nothing {
    throw KamlException(message)
}

data class KamlKonfig(
    val validationLevel: ValidationLevel
) {
    companion object {
        val default = KamlKonfig(ValidationLevel.default)
    }
}
