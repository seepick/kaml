KAML
=====

YAML generation with a shiny Kotlin DSL.

Note: This is different then [charleskorn's KAML](https://github.com/charleskorn/kaml/), which intention was (it's not maintained anymore) to add YAML support to kotlinx.serialization.

For Developers
====

* don't run `./gradlew test` as it will look for JUnit platform, but we are using the Kotest engine, thus run: `./gradlew jvmKotest`
