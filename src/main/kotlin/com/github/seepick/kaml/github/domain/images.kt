package com.github.seepick.kaml.github.domain

import arrow.optics.optics
import com.github.seepick.kaml.Image

@optics
data class RuntimeImage(val image: Image) : Image by image {
    companion object {} // for optics
}

enum class Runtime(val image: RuntimeImage) {
    UbuntuLatest(Image.ubuntuLatest);

    companion object {
        val default = UbuntuLatest
    }
}

@Deprecated("Use Image instead")
object Images {

    object Github {
        /** actions/checkout@v4 */
        val checkout = Image(
            group = "actions",
            name = "checkout",
            version = "v4",
        )

        val setupJava = Image(
            group = "actions",
            name = "setup-java",
            version = "v4",
        )
    }
}

