package com.github.seepick.kaml

data class GenericImage(
    override val group: String? = null,
    override val name: String,
    override val version: String? = null // or "latest"
) : Image
