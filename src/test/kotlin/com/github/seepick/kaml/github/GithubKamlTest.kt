package com.github.seepick.kaml.github

import com.github.seepick.kaml.Kaml
import com.github.seepick.kaml.github.domain.Images.checkout
import com.github.seepick.kaml.github.domain.Runtime
import com.github.seepick.kaml.github.dsl.github
import com.github.seepick.kaml.github.yaml.toYamlString
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.string.shouldContain

class GithubKamlTest : DescribeSpec({

    describe("Global configs") {
        it("name") {
            Kaml.github {
                name = "Continuous Integration"
            }.toYamlString() shouldContain
                """
                name: Continuous Integration
                """.trimIndent()
        }
    }
    describe("Triggers") {
        it("on push branches") {
            Kaml.github {
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
            Kaml.github {
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
            Kaml.github {
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
            Kaml.github {
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
            Kaml.github {
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
