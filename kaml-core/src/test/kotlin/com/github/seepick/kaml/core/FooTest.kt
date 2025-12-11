package com.github.seepick.kaml.core

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equals.shouldBeEqual

class FooTest : StringSpec( {
    "alive" {
        (21 * 2 ) shouldBeEqual 42
    }
})
