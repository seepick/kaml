plugins {
    `kotlin-dsl`
}

repositories {
    mavenLocal()
    gradlePluginPortal()
}

dependencies {
    val kotlinVersion = "2.2.21"
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")

    val koverVersion = "0.9.3"
    implementation("org.jetbrains.kotlinx:kover-gradle-plugin:$koverVersion")
    val manesVersion = "0.52.0"
    implementation("com.github.ben-manes.versions:com.github.ben-manes.versions.gradle.plugin:$manesVersion")

    val kotestVersion = "6.0.7"
    implementation("io.kotest:io.kotest.gradle.plugin:6.0.7")
}
