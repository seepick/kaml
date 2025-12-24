package com.github.seepick.kaml

import com.github.seepick.kaml.yaml.fixDashPlacement
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equals.shouldBeEqual

class MiscTest : StringSpec({
    "fixDashPlacement" {
        fixDashPlacement("  -\n    name: X") shouldBeEqual "  - name: X"
    }
})
