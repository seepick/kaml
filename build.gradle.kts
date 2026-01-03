import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    kotlin("jvm")
    id("maven-publish")
    id("io.kotest") // execute `gw kotest` instead `gw test`
//    id("org.jetbrains.kotlinx.kover")
    id("com.github.ben-manes.versions")
    id("com.google.devtools.ksp") version "2.3.4" // for optics generation
}

repositories {
    mavenCentral()
}

group = "com.github.seepick.kaml"
version = "1.1.0"

dependencies {
    implementation("com.amihaiemil.web:eo-yaml:8.0.6")
    implementation(kotlin("reflect"))
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.13")
    runtimeOnly("ch.qos.logback:logback-classic:1.5.18")
    // implementation(platform("io.arrow-kt:arrow-stack:$arrowVersion"))
    val arrowVersion = "2.2.1.1"
    implementation("io.arrow-kt:arrow-optics:$arrowVersion")
    ksp("io.arrow-kt:arrow-optics-ksp-plugin:$arrowVersion")

    listOf("framework-engine", "runner-junit5", "assertions-core", "property").forEach { artifactId ->
        testImplementation("io.kotest:kotest-$artifactId:6.0.7")
    }
}

kotlin {
    jvmToolchain(17)
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
    // already done by some (optics?) auto-magic
//    sourceSets.main {
//        kotlin.srcDir("build/generated/ksp/main/kotlin")
//    }
//    sourceSets.test {
//        kotlin.srcDir("build/generated/ksp/test/kotlin")
//    }
}
/*
idea {
    module {
        // Not using += due to https://github.com/gradle/gradle/issues/8749
        sourceDirs = sourceDirs + file("build/generated/ksp/main/kotlin") // or tasks["kspKotlin"].destination
        testSourceDirs = testSourceDirs + file("build/generated/ksp/test/kotlin")
        generatedSourceDirs = generatedSourceDirs + file("build/generated/ksp/main/kotlin") + file("build/generated/ksp/test/kotlin")
    }
}
 */
java {
    withSourcesJar()
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            // Uses Kotlin/JVM or Java components automatically
            from(components["java"])

            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            pom {
                name.set(project.name)
                description.set("Part of the ${rootProject.name} project")
                url.set("https://github.com/seepick/kaml")
            }
        }
    }
}

// Kover produces a JaCoCo-compatible XML at `build/reports/kover/report.xml` by default.
//kover {
//    // automatically attached to check target
//    // https://kotlin.github.io/kotlinx-kover/gradle-plugin/
//    useJacoco()
//    reports {
//        verify {
//            rule {
//                // line coverage
//                minBound(
//                    when (project.name) {
//                        // would be usually >80% ;)
//                        "app" -> 1
//
//                        else -> 3
//                    },
//                )
//            }
//        }
//    }
//}

tasks.withType<DependencyUpdatesTask> {
    val rejectPatterns = listOf(".*-ea.*", ".*RC", ".*M1", ".*check", ".*dev.*", ".*[Bb]eta.*", ".*[Aa]lpha.*")
        .map { Regex(it) }
    rejectVersionIf {
        rejectPatterns.any {
            it.matches(candidate.version)
        }
    }
}
