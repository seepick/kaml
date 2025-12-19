package com.github.seepick.kaml

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.equals.shouldBeEqual

class ImageTest : DescribeSpec({
    describe("format") {
        it("docker") {
            GenericImage("group", "name", "version").format(ImageFormatter.Docker) shouldBeEqual "group:name:version"
        }
        it("github") {
            GenericImage("group", "name", "version").format(ImageFormatter.Github) shouldBeEqual "group/name@version"
        }
    }
    describe("parse") {
        it("name only") {
            Image.parse("imageName", ImageFormatter.Github) shouldBeEqual GenericImage(name = "imageName")
        }
        it("group and name") {
            Image.parse("group/name", ImageFormatter.Github) shouldBeEqual GenericImage(group = "group", name = "name")
        }
        it("name and version") {
            Image.parse("name@version", ImageFormatter.Github) shouldBeEqual GenericImage(name = "name", version = "version")
        }
        it("group and name and version") {
            Image.parse("group/name@version", ImageFormatter.Github) shouldBeEqual GenericImage(
                group = "group",
                name = "name",
                version = "version"
            )
        }
    }
})