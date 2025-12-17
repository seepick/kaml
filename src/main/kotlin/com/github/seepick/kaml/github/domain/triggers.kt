package com.github.seepick.kaml.github.domain

// see: https://docs.github.com/en/actions/reference/workflows-and-actions/workflow-syntax
sealed interface Trigger

data class OnPushBranchTrigger(
    val branchNames: List<String>,
) : Trigger

data class CronTrigger(
    /** E.g.: "0 0 * * *" */
    val pattern: String,
) : Trigger

object ManualTrigger : Trigger
