package com.github.seepick.kaml

interface Image {
    val group: String?
    val name: String
    val version: String?

    fun format(formatter: ImageFormatter = ImageFormatter.Docker): String =
        (group?.let { "$it${formatter.groupNameSplitSymbol}" }
            ?: "") + name + (version?.let { "${formatter.nameVersionSplitSymbol}$it" } ?: "")

    companion object {
        fun parse(input: String, formatter: ImageFormatter): GenericImage {
            val (group, rest) = if (input.contains(formatter.groupNameSplitSymbol)) {
                val (group, rest) = input.split(formatter.groupNameSplitSymbol, limit = 3)
                group to rest
            } else null to input
            val (name, version) = if (input.contains(formatter.nameVersionSplitSymbol)) {
                val (name, version) = rest.split(formatter.nameVersionSplitSymbol, limit = 2)
                name to version
            } else rest to null

            return GenericImage(group, name, version)
        }
    }
}

enum class ImageFormatter(
    val groupNameSplitSymbol: String,
    val nameVersionSplitSymbol: String,
) {
    Github("/", "@"),
    Docker(":", ":"),
}

data class GenericImage(
    override val group: String? = null,
    override val name: String,
    override val version: String? = null // or "latest"
) : Image
