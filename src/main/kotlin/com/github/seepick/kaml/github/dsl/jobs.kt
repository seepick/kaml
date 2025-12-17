package com.github.seepick.kaml.github.dsl

import com.github.seepick.kaml.github.domain.Environment
import com.github.seepick.kaml.github.domain.Job
import com.github.seepick.kaml.github.domain.Permission
import com.github.seepick.kaml.github.domain.Runtime
import com.github.seepick.kaml.github.domain.Step

@GithubDsl
class JobsDsl {
    val jobs = mutableListOf<Job>()

    fun job(code: JobDsl.() -> Unit) {
        jobs += JobDsl().apply(code).build()
    }

    fun build() = jobs
}

@GithubDsl
class JobDsl {
    var id: String = "defaultJobId"
    var name: String = "Default Job Name"
    var runsOn: Runtime = Runtime.default
    var permissions = emptyList<Permission>()
    var environment: Environment? = null
    private var steps = listOf<Step>()

    fun steps(code: StepsDsl.() -> Unit) {
        steps = StepsDsl().apply(code).steps
    }

    fun permissions(code: PermissionsDsl.() -> Unit) {
        permissions = PermissionsDsl().apply(code).build()
    }

    fun build() = Job(
        id = id,
        name = name,
        environment = environment,
        permissions = permissions,
        runsOn = runsOn,
        steps = steps,
    )
}
