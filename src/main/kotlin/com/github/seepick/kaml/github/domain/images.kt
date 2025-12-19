package com.github.seepick.kaml.github.domain

import com.github.seepick.kaml.GenericImage
import com.github.seepick.kaml.Image

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
