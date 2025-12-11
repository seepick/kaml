package com.github.seepick.kaml.github

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equals.shouldBeEqual

class BarTest : StringSpec( {
    "alive" {
        (21 * 2 ) shouldBeEqual 42
    }
})