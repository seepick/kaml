package com.github.seepick.kaml


object Locator

fun loadTestResource(path: String): String {
    val file = Locator::class.java.getResource(path) ?: error("Resource not found: $path")
    return file.readText().dropLastWhile { it == '\n' } // intellij autoformatter hack ;)
}
