Kaml
========================================================================================================================

A Kotlin Yaml generator offering a concise, typesafe, "auto-completable" DSL.

Just kidding, this is only a fun learning project 😜
Use your favorite IDE with YAML support and preferably a sophisticated AI to help you write YAML.

_Note_: This is different then [charleskorn's KAML](https://github.com/charleskorn/kaml/), which intention was (it's not
maintained anymore) to add YAML support to kotlinx.serialization.

Benefits
------------------------------------------------------------------------------------------------------------------------

* *Typesafe*: no more "misplaced indentation" and hours/days of painstaking debugging (use statically typed language and
  compiler checks)
* _*Auto-completable*_: lowering entrance barrier for new learning (not having to remember all options)
* From run-time (deploy and wait) to write-time (instant compiler) feedback
* Use code features and introduce custom abstraction layer on top (specific, reusable functionality; extension
  functions, parameterized blocks)
* YAML is inlined/flattened-out and always see what's actually happening
    - no include templating mechanism (dislocated), but maybe add support mode?
    - inclusions are done in high-level kotlin DSL,
* Possibility for more sophisticated high-level semantic *checks*
* Low risk: Can be dropped at any time and continue hand written YAML files instead
* DSLs available for: OpenAPI, GitHub Actions, Azure DevOps, Kubernetes, OpenShift, ...

PS: A Kotlin DSL offers basically the same level of conciseness as a YAML file (speaking in terms of LoC), but its
advantages lies in other areas.

Howto
------------------------------------------------------------------------------------------------------------------------

Example of a Kubernetes Deployment manifest:

```kotlin
Kaml.k8s.deployment {
    metadata {
        name = "my-deployment"
    }
    selector {
        matchLabels += "app" to "my-container"
    }
    replicas = 2
    template {
        metadata {
            labels += "app" to "my-container"
        }
        container {
            name = "my-container"
            image = Image(name = "my-image", version = "latest")
        }
    }
} saveYamlTo File("deployment.yaml")
```

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
} saveYamlTo File("continuous.yaml")
```

Or get the Yaml string yourself by invoking `toYaml()` and do how it seems fit for you.

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

Developer Hints
========================================================================================================================

* You need to have GitHub credentials be configured in your local `~/.gradle/gradle.properties` file.
* Run `./gradlew kotest` (Kotest engine) instead of the usual `./gradlew test` (JUnit platform).

Research
========================================================================================================================

[Yaml specification](https://github.com/seepick/kaml/blob/main/yaml_spec.md) summarized.

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
