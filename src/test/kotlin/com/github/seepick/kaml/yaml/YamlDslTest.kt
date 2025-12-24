package com.github.seepick.kaml.yaml

import com.amihaiemil.eoyaml.YamlNode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.spec.style.scopes.DescribeSpecContainerScope
import io.kotest.matchers.equals.shouldBeEqual

class YamlDslTest : DescribeSpec({

    suspend fun DescribeSpecContainerScope.test(testName: String, pair: Pair<() -> YamlNode, String>) {
        it(testName) {
            pair.first.invoke().toCleanYamlString() shouldBeEqual pair.second
        }
    }

    describe("Map") {

        test("single string value", {
            YamlRoot.map {
                add("foo", "bar")
            }
        } to """
                foo: bar
            """.trimIndent())
        test("more single types", {
            YamlRoot.map {
                add("foo", 1)
                add("bar", true)
                add("baz", 4.21)
            }
        } to """
                foo: 1
                bar: true
                baz: 4.21
            """.trimIndent())

        test("nested", {
            YamlRoot.map {
                map("super") {
                    add("sub", "val")
                }
            }
        } to """
                super:
                  sub: val
            """.trimIndent())
    }

    describe("List") {

        test("simple", {
            YamlRoot.list {
                add("item1")
                add("item2")
            }
        } to """
                - item1
                - item2
            """.trimIndent())

        test("simple", {
            YamlRoot.list {
                add("item1")
                add("item2")
            }
        } to """
                - item1
                - item2
            """.trimIndent())
    }
})