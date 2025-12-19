package com.github.seepick.kaml

import java.io.File

object Kaml // extension functions

fun interface KamlYamlOutput {
    fun toYaml(): String
}


@DslMarker
annotation class KamlDsl

infix fun KamlYamlOutput.saveYamlTo(target: File) {
    target.writeText(toYaml())
}

class KamlException internal constructor(message: String) : RuntimeException(message)

internal fun kerror(message: String): Nothing {
    throw KamlException(message)
}
