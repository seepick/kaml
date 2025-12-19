Kaml
========================================================================================================================

A Kotlin Yaml generator offering a concise, typesafe, "auto-completable" DSL.

Advantages:

* *Typesafe*: no more "misplaced indentation" and hours/days of painstaking debugging
* _*Auto-completable*_: lowering entrance barrier for new learning
* Possibility for more sophisticated high-level semantic *checks*
* YAML is inlined/flattened-out and always see what's actually happening
    - no include templating mechanism (dislocated), but maybe add support mode?
    - inclusions are done in high-level kotlin DSL,
* Low risk: Can be dropped at any time and continue hand writing
* DSLs available for: OpenAPI, GitHub Actions, Azure DevOps, Kubernetes, OpenShift, ...

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

Deployed to [Github Packages](https://docs.github.com/en/packages):

```kotlin
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/seepick/kaml")
        name = "KAML GitHubPackages"
        credentials {
            // search in local ~/gradle.properties or in env-vars:
            username = project.findProperty("githubPackages.user") as? String ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("githubPackages.key") as? String ?: System.getenv("GITHUB_TOKEN")
            // and in your GitHub Actions workflow:
            // env:
            //   GITHUB_ACTOR: ${{ github.actor }}
            //   GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
        }
    }
}

dependencies {
    implementation("com.github.seepick.kaml:kaml-xxx:X.X.X")
}
```

Note: This is different then [charleskorn's KAML](https://github.com/charleskorn/kaml/), which intention was (it's not maintained anymore) to add YAML support to kotlinx.serialization.


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
