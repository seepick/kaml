Kaml
========================================================================================================================

A Kotlin Yaml generator offering a concise, typesafe, "auto-completable" DSL.

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
========================================================================================================================

* Kaml for OpenAPI
* Kaml for OpenShift
* Kaml for Kustomize
* Kaml for ...

GitHub
------------------------------------------------------------------------------------------------------------------------

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

Yaml Spec
========================================================================================================================

Referring to: [Yaml spec 1.2.2](https://yaml.org/spec/1.2.2/)

See cheatsheet: https://yaml.org/refcard.html

Basics
------------------------------------------------------------------------------------------------------------------------

Strings do not require quotation marks.
Double quotes support escape sequences, single quotes not:

```yaml
unicode: "Sosa did fine.\u263A"
control: "\b1998\t1999\t2000\n"

single: '"Howdy!" he cried.'
quoted: ' # Not a ''comment''.'
```

All flow scalars can span multiple lines; line breaks are always folded.

```yaml
plain:
  This unquoted scalar
  spans many lines.

quoted: "So does this
  quoted scalar.\n"
```

Mappings
------------------------------------------------------------------------------------------------------------------------

Scalar to scalar:

```yaml
key: value
port: 42
weight: 1.337
flag: true
orNot: null
```

... or the inline (instead of indented) format:

```yaml
{ key: value, port: 42 }
```

Scalar sequence:

```yaml
- Mark McGwire
- Sammy Sosa
- Ken Griffey
```

... or the inline (instead of indented) format:

```yaml
[ foo, bar, baz ]
```

Scalar to sequence:

```yaml
foo:
  - bar
  - baz
```

Mappings:

```yaml
- name: Mark McGwire
  age: 65
```

Sequence of sequence:

```yaml
- [ name , age ]
- [ foo  ,  12 ]
- [ bar  ,  42 ]
```

Mapping of mappings:

```yaml
Foo: { bar: 42, baz: true }
```

Ways of defining a list of key-value objects:

```yaml
- { name: John Smith, age: 33 }
- name: Mary Smith
  age: 27
- [ name, age ]: [ Rae Smith, 4 ]
```

Structures
------------------------------------------------------------------------------------------------------------------------

directives before doc separator `---`, and doc finalizer `...`:

```yaml
# some directives
---
- val1
- val2

...
# 2nd document
---
- foobar
```

references marked with `&` and then referenced with `*`:

```yaml
obj1:
  - Foo
  - &SS Bar
obj2:
  - *SS
  - Baz
```

Complex mapping key / Mapping between Sequences:

```text
? - Detroit Tigers
  - Chicago cubs
: - 2001-07-23

? [ New York Yankees,
    Atlanta Braves ]
: [ 2001-07-02, 2001-08-12, 
    2001-08-14 ]
```

Compact nested mapping:

```yaml
- foo: 42
  bar: baz
```

Scalars
----

Newlines are preserved with `|` (literal style):

```yaml
keepLines: |
  \//||\/||
  // ||  ||__
```

Newlines become spaces with `>` (folded style):

```yaml
flowText: >
  Mark McGwire's
  year was crippled
  by a knee injury.
```

... unless it ends an empty/more-indented line:

```yaml
structuredText: >
  Sammy Sosa completed another
  fine season with great stats.

    63 Home Runs
    0.288 Batting Average

  What a year!
```

Explicit type declaration:

```yaml
a: 123                     # an integer
b: "123"                   # a string, disambiguated by quotes
c: 123.0                   # a float
d: !!float 123             # also a float via explicit data type prefixed by (!!)
e: !!str 123               # a string, disambiguated by explicit type
f: !!str Yes               # a string via explicit type
g: Yes                     # a Boolean True (yaml1.1), string "Yes" (yaml1.2)
h: Yes we have No bananas  # a string, "Yes" and "No" disambiguated by context.
```
