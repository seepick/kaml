package com.github.seepick.kaml.github.domain

interface Image {
    val group: String?
    val name: String
    val version: String?

    val coordinates get() = (group?.let { "$it/" } ?: "") + name + (version?.let { "@$it" } ?: "")
}

data class GenericImage(
    override val group: String? = null,
    override val name: String,
    override val version: String? = null
) : Image

data class RuntimeImage(val image: Image) : Image by image

enum class Runtime(val image: RuntimeImage) {
    UbuntuLatest(Images.ubuntuLatest);

    companion object {
        val default = UbuntuLatest
    }
}

object Images {
    /** actions/checkout@v4 */
    val checkout = GenericImage(
        group = "actions",
        name = "checkout",
        version = "v4",
    )

    val setupJava = GenericImage(
        group = "actions",
        name = "setup-java",
        version = "v4",
    )

    val ubuntuLatest = RuntimeImage(
        GenericImage(
            name = "ubuntu-latest",
        ),
    )
}
