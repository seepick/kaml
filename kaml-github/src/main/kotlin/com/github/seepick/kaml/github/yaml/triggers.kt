package com.github.seepick.kaml.github.yaml

import com.amihaiemil.eoyaml.YamlMapping
import com.github.seepick.kaml.core.addAllStrings
import com.github.seepick.kaml.core.yamlMap
import com.github.seepick.kaml.core.yamlScal
import com.github.seepick.kaml.core.yamlSeq
import com.github.seepick.kaml.github.domain.CronTrigger
import com.github.seepick.kaml.github.domain.ManualTrigger
import com.github.seepick.kaml.github.domain.OnPushBranchTrigger
import com.github.seepick.kaml.github.domain.Trigger

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
