package com.github.seepick.kaml.github.domain

data class GithubAction(
    val name: String,
    val triggers: List<Trigger>,
    val jobs: List<Job>
) {
    //  TODO configurable sanity check: { none, warn, strict/fail }?
//    init {
//        require(name.isNotEmpty()) { "Name must not be empty!" }
//        require(triggers.isNotEmpty()) { "At least 1 trigger is required!" }
//        require(jobs.isNotEmpty()) { "At least 1 job is required!" }
//    }
}

data class Job(
    val id: String,
    val name: String,
    val runsOn: Runtime,
    val environment: Environment?,
    val permissions: List<Permission>,
    val steps: List<Step>,
)

interface Environment {
    val yamlValue: String
}
