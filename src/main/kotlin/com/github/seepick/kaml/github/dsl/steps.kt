package com.github.seepick.kaml.github.dsl

import com.github.seepick.kaml.JavaVersion
import com.github.seepick.kaml.github.domain.GenericStep
import com.github.seepick.kaml.github.domain.Images
import com.github.seepick.kaml.github.domain.RunStep
import com.github.seepick.kaml.github.domain.Step

@GithubDsl
class StepsDsl {
    // if own module, then these internal are not seen (no need for interface abstraction)
    internal val steps = mutableListOf<Step>()

    fun checkout(code: CheckoutDsl.() -> Unit) {
        steps += CheckoutDsl().apply(code).build()
    }

    fun setupJava(code: SetupJavaDsl.() -> Unit) {
        steps += SetupJavaDsl().apply(code).build()
    }

    fun runCommand(code: RunCommandDsl.() -> Unit) {
        steps += RunCommandDsl().apply(code).build()
    }

    fun build() = steps
}

@GithubDsl
class CheckoutDsl {
    var name: String = "Checkout Code"

    fun build() = GenericStep(
        name = name,
        uses = Images.checkout,
    )
}

enum class Distribution(val yamlValue: String) {
    Temurin("temurin");

    companion object {
        val default = Temurin
    }
}

@GithubDsl
class SetupJavaDsl {
    var name: String = "Setup JDK"
    var distribution: Distribution = Distribution.default
    var javaVersion: JavaVersion = JavaVersion.v17

    fun build() = GenericStep(
        name = name,
        uses = Images.setupJava,
        withParams = mapOf(
            "distribution" to distribution.yamlValue,
            "java-version" to javaVersion.yamlValue,
        ),
    )
}

@GithubDsl
class RunCommandDsl {
    var name: String = "Run Command"
    var command: String = "echo \"No actual run command defined!\";"

    fun build() = RunStep(
        name = name,
        command = command,
    )
}
