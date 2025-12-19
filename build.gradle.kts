import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    kotlin("jvm")
    id("maven-publish")
    id("io.kotest") // execute `gw kotest` instead `gw test`
//    id("org.jetbrains.kotlinx.kover")
    id("com.github.ben-manes.versions")
}

repositories {
    mavenCentral()
}

group = "com.github.seepick.kaml"
version = "1.1.0"

dependencies {
    implementation("com.amihaiemil.web:eo-yaml:8.0.6")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.13")

    listOf("framework-engine", "runner-junit5", "assertions-core", "property").forEach { artifactId ->
        testImplementation("io.kotest:kotest-$artifactId:6.0.7")
    }
}

kotlin {
    jvmToolchain(17)
}

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
                url.set("https://github.com/seepick/kaml") // TODO use github pages URL instead
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
