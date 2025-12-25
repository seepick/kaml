package com.github.seepick.kaml

import java.io.File

object Kaml // extension functions
class XKaml(val konfig: Konfig = Konfig.default)

fun interface KamlYamlOutput {
    fun toYaml(): String
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

data class Konfig(
    val validationLevel: ValidationLevel
) {
    companion object {
        val default = Konfig(ValidationLevel.default)
    }
}
