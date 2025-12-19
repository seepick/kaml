Kaml
========================================================================================================================

A Kotlin Yaml generator offering a concise, typesafe, "auto-completable" DSL.

*Advantages*

* *Typesafe*: no more "misplaced indentation" and hours/days of painstaking debugging
* _*Auto-completable*_: lowering entrance barrier for new learning
* Possibility for more sophisticated high-level semantic *checks*
* YAML is inlined/flattened-out and always see what's actually happening
    - no include templating mechanism (dislocated), but maybe add support mode?
    - inclusions are done in high-level kotlin DSL,
* Low risk: Can be dropped at any time and continue hand writing
* DSLs available for: OpenAPI, GitHub Actions, Azure DevOps, Kubernetes, OpenShift, ...

_Note_: This is different then [charleskorn's KAML](https://github.com/charleskorn/kaml/), which intention was (it's not maintained anymore) to add YAML support to kotlinx.serialization.

Howto
------------------------------------------------------------------------------------------------------------------------

Example of a GitHub action with Kaml:

```kotlin
githubKaml {
    name = "Continuous"
    triggers {
        onPushBranches("main")
    }
    jobs {
        job {
            id = "ci"
            name = "Continuous Integration Job"
            // runs by default with ubuntu-latest
            permissions {
                contents = PermissionLevel.Read
            }
            steps {
                checkout {}
                setupJava {
                    javaVersion = JavaVersion.v17
                }
                runCommand {
                    name = "Run Gradle 'check' task"
                    command = "./gradlew check"
                }
            }
        }
    }
}
```

Then execute invoke the DSLs `.toYamlString()` and save it to a file of your choice.

Setup
------------------------------------------------------------------------------------------------------------------------

Add the JitPack repository to your `settings.gradle.kts`:

```kotlin
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

Add the dependency to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.seepick:kaml:v1.0.0")
}
```

Lookup the latest version on the [JitPack website](https://jitpack.io/#seepick/kaml).

Todos
------------------------------------------------------------------------------------------------------------------------

Ad GitHub Actions:

* multi-line strings (preserved | and folded >)
* extensibility: allow for totally custom yaml entries
* support comments
* showcase building layer on top of DSL (reuse, reference, ...)
* generation modes: 1) inline 2) reuse/reference (the typical way when handwriting them)

Developer Hints
========================================================================================================================

* You need to have GitHub credentials be configured in your local `~/.gradle/gradle.properties` file.
* Run `./gradlew kotest` (Kotest engine) instead of the usual `./gradlew test` (JUnit platform).

Research
========================================================================================================================

Yaml Libs
------------------------------------------------------------------------------------------------------------------------

Plain generators:

* eo-yaml
    * ideal for creating yaml programmatically; similar to JSON-P
* BoostedYAML
    * for config files + lifecycle management
* Simple‑YAML
    * straight forward; for generating or manipulating YAML configuration

Object mapper:

* SnakeYaml
    * https://bitbucket.org/snakeyaml/snakeyaml-engine/src/master/
    * defacto standard
* BoostedYAML
    * sits on top of SnakeYaml
* Kaml, no longer maintained!
    * https://github.com/charleskorn/kaml (different goal: yaml support for kotlinx.serialization)
    * using SnakeYaml for
      KMP: https://github.com/charleskorn/kaml/blob/main/src/commonMain/kotlin/com/charleskorn/kaml/YamlOutput.kt
* jackson-dataformat-yaml
* YamlBeans: too simple, only object de/serialization
