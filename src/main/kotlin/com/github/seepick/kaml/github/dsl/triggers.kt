package com.github.seepick.kaml.github.dsl

import com.github.seepick.kaml.github.domain.CronTrigger
import com.github.seepick.kaml.github.domain.ManualTrigger
import com.github.seepick.kaml.github.domain.OnPushBranchTrigger
import com.github.seepick.kaml.github.domain.Trigger

class TriggersDsl {
    private val triggers = mutableListOf<Trigger>()

    fun onPushBranches(branch: String, vararg moreBranches: String) {
        triggers += OnPushBranchTrigger(
            buildList {
                add(branch)
                addAll(moreBranches)
            },
        )
    }

    /**
     * Schedule a job to run at a specific time ((Cron Pattern Documentation)[https://pubs.opengroup.org/onlinepubs/9699919799/utilities/crontab.html#tag_20_25_07]).
     *
     * @param pattern "0 0 * * *" (minute, hour, day, month, weekday)
     */
    fun cron(pattern: String) {
        triggers += CronTrigger(pattern)
    }

    fun manual() {
        triggers += ManualTrigger
    }

    fun build(): List<Trigger> = triggers
}
