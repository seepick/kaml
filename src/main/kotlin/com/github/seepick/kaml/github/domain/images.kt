package com.github.seepick.kaml.github.domain

import com.github.seepick.kaml.Image

data class RuntimeImage(val image: Image) : Image by image

enum class Runtime(val image: RuntimeImage) {
    UbuntuLatest(Images.ubuntuLatest);

    companion object {
        val default = UbuntuLatest
    }
}

object Images {

    val nginx = Image(name = "nginx")

    val ubuntuLatest = RuntimeImage(Image(name = "ubuntu-latest"))

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

