package com.github.seepick.kaml.github

import com.github.seepick.kaml.JavaVersion
import com.github.seepick.kaml.Kaml
import com.github.seepick.kaml.github.domain.PermissionLevel
import com.github.seepick.kaml.github.domain.Runtime
import com.github.seepick.kaml.github.dsl.DemoEnvironmentNamespace
import com.github.seepick.kaml.github.dsl.Distribution
import com.github.seepick.kaml.github.dsl.github
import com.github.seepick.kaml.github.yaml.toYamlString
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.string.shouldContain

class GithubTest : DescribeSpec({

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
    describe("Full example") {
        it("full example") {
            Kaml.github {
                name = "FTC Yaml Name"
                triggers {
                    onPushBranches("FTC_branch")
                }
                jobs {
                    job {
                        id = "ftcJobId"
                        name = "FTC Job Name"
                        runsOn = Runtime.UbuntuLatest
                        environment = DemoEnvironmentNamespace.Production
                        permissions {
                            contents = PermissionLevel.Read
                        }
                        steps {
                            checkout {
                                name = "FTC Checkout Name"
                            }
                            setupJava {
                                name = "FTC SetupJava Name"
                                distribution = Distribution.Temurin
                                javaVersion = JavaVersion.v17
                            }
                            runCommand {
                                name = "FTC Run Command"
                                command = "./ftc run"
                            }
                        }
                    }
                }
            }.toYamlString() shouldBeEqual """
                name: FTC Yaml Name
                on:
                  push:
                    branches:
                      - FTC_branch
                jobs:
                  ftcJobId:
                    name: FTC Job Name
                    runs-on: ubuntu-latest
                    environment: prod
                    permissions:
                      contents: read
                    steps:
                      - name: FTC Checkout Name
                        uses: "actions/checkout@v4"
                      - name: FTC SetupJava Name
                        uses: "actions/setup-java@v4"
                        with:
                          distribution: temurin
                          java-version: 17
                      - name: FTC Run Command
                        run: ./ftc run
            """.trimIndent()
        }
    }
})
