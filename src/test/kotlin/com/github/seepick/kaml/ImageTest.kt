package com.github.seepick.kaml

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.equals.shouldBeEqual

class ImageTest : DescribeSpec({
    describe("format") {
        it("docker") {
            Image("group", "name", "version").format(ImageFormatter.Docker) shouldBeEqual "group:name:version"
        }
        it("github") {
            Image("group", "name", "version").format(ImageFormatter.Github) shouldBeEqual "group/name@version"
        }
    }
    describe("parse") {
        it("name only") {
            Image.parse("imageName", ImageFormatter.Github) shouldBeEqual Image(name = "imageName")
        }
        it("group and name") {
            Image.parse("group/name", ImageFormatter.Github) shouldBeEqual Image(group = "group", name = "name")
        }
        it("name and version") {
            Image.parse("name@version", ImageFormatter.Github) shouldBeEqual Image(name = "name", version = "version")
        }
        it("group and name and version") {
            Image.parse("group/name@version", ImageFormatter.Github) shouldBeEqual Image(
                group = "group",
                name = "name",
                version = "version"
            )
        }
    }
})