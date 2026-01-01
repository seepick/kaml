package com.github.seepick.kaml.k8s.shared

import com.github.seepick.kaml.KamlDsl

@KamlDsl
class SelectorDsl {
    val matchLabels = mutableMapOf<String, String>()
    fun build() = Selector(
        matchLabels = matchLabels
    )
}

data class Selector(
    val matchLabels: Map<String, String>,
) {
    companion object {
        val default = Selector(emptyMap())
    }

    fun isEmpty() = matchLabels.isEmpty()
    fun isNotEmpty() = matchLabels.isNotEmpty()
}
