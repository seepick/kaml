package com.github.seepick.kaml

import com.github.seepick.kaml.github.dsl.github
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.file.shouldNotExist
import io.kotest.matchers.should
import io.kotest.matchers.string.shouldContain
import java.io.File

class SaveAsTest : StringSpec({
    val testSignal = "testSignal"
    "save as saves" {
        val tmp = File.createTempFile("kaml-test", "saveas")
        tmp.delete()
        tmp.shouldNotExist()

        Kaml.github { name = testSignal } saveYamlTo tmp

        tmp should {
            it.shouldExist()
            it.readText() shouldContain testSignal
        }
    }
})