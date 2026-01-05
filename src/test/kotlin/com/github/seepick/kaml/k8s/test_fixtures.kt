package com.github.seepick.kaml.k8s

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.Kaml
import com.github.seepick.kaml.k8s.artifacts.deployment.deployment
import io.kotest.property.Arb
import io.kotest.property.arbitrary.Codepoint
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.az
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.orNull
import io.kotest.property.arbitrary.string

fun Arb.Companion.id() = arbitrary {
    string(1..10, codepoints = Codepoint.az()).bind()
}

fun Arb.Companion.image() = arbitrary {
    Image(name = id().bind(), group = id().orNull().bind(), version = id().orNull().bind())
}

fun Arb.Companion.deployment() = arbitrary {
    // bind() is suspended, thus not callable within deployment {}
    val id = id().bind()
    val image = image().bind()
    val replicas = int(1..3).bind()
    Kaml.k8s.deployment {
        metadata {
            name = "$id-deployment"
        }
        this.replicas = replicas
        template {
            metadata {
                name = "$id-pod"
            }
            container {
                name = "$id-container"
                this.image = image
            }
        }
    }
}
