package com.github.seepick.kaml.github.dsl

import com.github.seepick.kaml.Checkable
import com.github.seepick.kaml.Kaml
import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.github.domain.Environment
import com.github.seepick.kaml.github.domain.GithubAction
import com.github.seepick.kaml.github.domain.Job
import com.github.seepick.kaml.github.domain.Trigger

fun Kaml.github(code: GithubActionDsl.() -> Unit): GithubAction =
    GithubActionDsl().apply(code).build()

@KamlDsl
class GithubActionDsl : Checkable {
    /** The visible name of the action in the GitHub UI. */
    var name: String = "Default Action Name"
    private var triggersList = emptyList<Trigger>()
    private var jobsList = emptyList<Job>()

    /**
     * Under which circumstances this action should run ([GitHub trigger documentation](https://docs.github.com/en/actions/reference/workflows-and-actions/events-that-trigger-workflows)).
     */
    fun triggers(code: TriggersDsl.() -> Unit) {
        triggersList = TriggersDsl().apply(code).build()
    }

    /** An action is subdivided into jobs (and further into steps). */
    fun jobs(code: JobsDsl.() -> Unit) {
        jobsList = JobsDsl().apply(code).build()
    }

    internal fun build() = GithubAction(
        name = name,
        triggers = triggersList,
        jobs = jobsList,
    )
}

// should be declared outside (custom) by the project
enum class DemoEnvironmentNamespace(override val yamlValue: String) : Environment {
    Production("prod"),
    Acceptance("acc"),
    Test("test"),
    Development("dev"),
}
