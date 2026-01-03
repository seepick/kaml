package com.github.seepick.kaml

import arrow.optics.optics
import com.github.seepick.kaml.github.domain.RuntimeImage

interface Image {
    val group: String?
    val name: String
    /** or "latest" */
    val version: String?

    fun format(formatter: ImageFormatter = ImageFormatter.Docker): String =
        (group?.let { "$it${formatter.groupNameSplitSymbol}" }
            ?: "") + name + (version?.let { "${formatter.nameVersionSplitSymbol}$it" } ?: "")

    companion object {
        val nginx = Image(name = "nginx")
        val ubuntuLatest = RuntimeImage(Image(name = "ubuntu-latest"))
        val postgres = Image("postgres", version = "18.1-alpine")

        fun parse(input: String, formatter: ImageFormatter): Image {
            val (group, rest) = if (input.contains(formatter.groupNameSplitSymbol)) {
                val (group, rest) = input.split(formatter.groupNameSplitSymbol, limit = 3)
                group to rest
            } else null to input
            val (name, version) = if (input.contains(formatter.nameVersionSplitSymbol)) {
                val (name, version) = rest.split(formatter.nameVersionSplitSymbol, limit = 2)
                name to version
            } else rest to null

            return Image(name, group, version)
        }

        operator fun invoke(name: String, group: String? = null, version: String? = null) =
            GenericImage(name, group, version)
    }
}

@optics
data class GenericImage(
    override val name: String,
    override val group: String?,
    override val version: String?,
) : Image {
    companion object {} // for optics
}

enum class ImageFormatter(
    val groupNameSplitSymbol: String,
    val nameVersionSplitSymbol: String,
) {
    Github("/", "@"),
    Docker(":", ":"),
}
