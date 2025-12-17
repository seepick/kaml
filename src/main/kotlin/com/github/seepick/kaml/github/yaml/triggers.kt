package com.github.seepick.kaml.github.yaml

import com.amihaiemil.eoyaml.YamlMapping
import com.github.seepick.kaml.addAllStrings
import com.github.seepick.kaml.github.domain.CronTrigger
import com.github.seepick.kaml.github.domain.ManualTrigger
import com.github.seepick.kaml.github.domain.OnPushBranchTrigger
import com.github.seepick.kaml.github.domain.Trigger
import com.github.seepick.kaml.yamlMap
import com.github.seepick.kaml.yamlScal
import com.github.seepick.kaml.yamlSeq

internal fun triggersYaml(triggers: List<Trigger>): YamlMapping {
    val rootTriggers = yamlMap()
    triggers.forEach { trigger ->
        when (trigger) {
            ManualTrigger -> {
                rootTriggers.add("workflow_dispatch", yamlScal().buildPlainScalar(""))
            }

            is CronTrigger -> {
                rootTriggers.add("schedule", yamlSeq().add(yamlMap().add("cron", trigger.pattern).build()).build())
            }

            is OnPushBranchTrigger -> {
                rootTriggers.add(
                    "push",
                    yamlMap().add(
                        "branches",
                        yamlSeq().addAllStrings(trigger.branchNames).build(),
                    ).build(),
                )
            }
        }
    }
    return rootTriggers.build()
}
