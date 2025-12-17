import com.github.seepick.kaml.core.fixDashPlacement
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equals.shouldBeEqual

class MiscTest : StringSpec( {
    "fixDashPlacement" {
        fixDashPlacement("  -\n    name: X") shouldBeEqual "  - name: X"
    }
})
