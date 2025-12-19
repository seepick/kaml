package com.github.seepick.kaml

interface Image {
    val group: String?
    val name: String
    /** or "latest" */
    val version: String?

    fun format(formatter: ImageFormatter = ImageFormatter.Docker): String =
        (group?.let { "$it${formatter.groupNameSplitSymbol}" }
            ?: "") + name + (version?.let { "${formatter.nameVersionSplitSymbol}$it" } ?: "")

    companion object {
        fun parse(input: String, formatter: ImageFormatter): Image {
            val (group, rest) = if (input.contains(formatter.groupNameSplitSymbol)) {
                val (group, rest) = input.split(formatter.groupNameSplitSymbol, limit = 3)
                group to rest
            } else null to input
            val (name, version) = if (input.contains(formatter.nameVersionSplitSymbol)) {
                val (name, version) = rest.split(formatter.nameVersionSplitSymbol, limit = 2)
                name to version
            } else rest to null

            return Image(group, name, version)
        }

        operator fun invoke(group: String? = null, name: String, version: String? = null) =
            GenericImage(group, name, version)
    }
}

data class GenericImage(
    override val group: String?,
    override val name: String,
    override val version: String?,
) : Image

enum class ImageFormatter(
    val groupNameSplitSymbol: String,
    val nameVersionSplitSymbol: String,
) {
    Github("/", "@"),
    Docker(":", ":"),
}
