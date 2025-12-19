package com.github.seepick.kaml.github.yaml

import com.amihaiemil.eoyaml.YamlMapping
import com.github.seepick.kaml.ImageFormatter
import com.github.seepick.kaml.addAllNodes
import com.github.seepick.kaml.github.domain.Job
import com.github.seepick.kaml.yamlMap
import com.github.seepick.kaml.yamlSeq
import kotlin.collections.map

internal fun jobsYaml(jobs: List<Job>): YamlMapping {
    val rootJobs = yamlMap()
    jobs.map { job ->
        val jobYaml = yamlMap()
            .add("name", job.name)
            .add("runs-on", job.runsOn.image.format())
        job.environment?.let { env ->
            jobYaml.add("environment", env.yamlValue)
        }
        if (job.permissions.isNotEmpty()) {
            jobYaml.add(
                "permissions",
                job.permissions.fold(yamlMap()) { node, permission ->
                    node.add(permission.type.yamlValue, permission.level.yamlValue)
                }.build(),
            )
        }
        if (job.steps.isNotEmpty()) {
            jobYaml.add(
                "steps",
                yamlSeq().addAllNodes(
                    job.steps.map {
                        stepYaml(it)
                    },
                ).build(),
            )
        }
        rootJobs.add(job.id, jobYaml.build())
    }
    return rootJobs.build()
}
