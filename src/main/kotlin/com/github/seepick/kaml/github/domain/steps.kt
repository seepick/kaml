package com.github.seepick.kaml.github.domain

import com.github.seepick.kaml.Image

sealed interface Step {
    val name: String
    val uses: Image?
}

data class RunStep(
    override val name: String,
    val command: String,
) : Step {
    override val uses: Image? = null
}

data class GenericStep(
    override val name: String,
    override val uses: Image,
    val withParams: Map<String, String> = emptyMap(),
) : Step
