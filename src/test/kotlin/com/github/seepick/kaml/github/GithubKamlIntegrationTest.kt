package com.github.seepick.kaml.github

import com.github.seepick.kaml.JavaVersion
import com.github.seepick.kaml.Kaml
import com.github.seepick.kaml.github.domain.PermissionLevel
import com.github.seepick.kaml.github.domain.Runtime
import com.github.seepick.kaml.github.dsl.DemoEnvironmentNamespace
import com.github.seepick.kaml.github.dsl.Distribution
import com.github.seepick.kaml.github.dsl.github
import com.github.seepick.kaml.github.yaml.toYamlString
import com.github.seepick.kaml.loadTestResource
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equals.shouldBeEqual

class GithubKamlIntegrationTest : StringSpec({

    fun loadResource(path: String): String = loadTestResource("/githubKaml/$path")

    "Full Test Continuous" {
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
        }.toYamlString() shouldBeEqual loadResource("continuous.yml")
    }
})
