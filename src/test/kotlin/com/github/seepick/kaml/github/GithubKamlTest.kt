package com.github.seepick.kaml.github

import com.github.seepick.kaml.github.domain.Runtime
import com.github.seepick.kaml.github.dsl.githubKaml
import com.github.seepick.kaml.github.yaml.toYamlString
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.string.shouldContain

class GithubKamlTest : DescribeSpec({

    describe("Global configs") {
        it("name") {
            githubKaml {
                name = "Continuous Integration"
            }.toYamlString() shouldContain
                """
                name: Continuous Integration
                """.trimIndent()
        }
    }
    describe("Triggers") {
        it("on push branches") {
            githubKaml {
                triggers {
                    onPushBranches("main")
                }
            }.toYamlString() shouldContain
                """
                |on:
                |  push:
                |    branches:
                |      - main
                """.trimMargin()
        }
        it("on cron") {
            githubKaml {
                triggers {
                    cron("0 0 * * *")
                }
            }.toYamlString() shouldContain
                """
                |on:
                |  schedule:
                |    - cron: "0 0 * * *"
                """.trimMargin()
        }
        it("on manual") {
            githubKaml {
                triggers {
                    manual()
                    // support input values
                }
            }.toYamlString() shouldContain
                """
                |on:
                |  workflow_dispatch:
                """.trimMargin()
        }
    }
    describe("Jobs") {
        it("general") {
            githubKaml {
                jobs {
                    job {
                        id = "jobId"
                        name = "Job Name"
                        runsOn = Runtime.UbuntuLatest
                    }
                }
            }.toYamlString() shouldContain
                """
                |jobs:
                |  jobId:
                |    name: Job Name
                |    runs-on: ubuntu-latest
                """.trimMargin()
        }
    }
    describe("Steps") {
        it("checkout") {
            githubKaml {
                jobs {
                    job {
                        steps {
                            checkout {}
                        }
                    }
                }
            }.toYamlString() shouldContain
                """
                |    steps:
                |      - name: Checkout Code
                |        uses: "actions/checkout@v4"
                """.trimMargin()
        }
    }
})
