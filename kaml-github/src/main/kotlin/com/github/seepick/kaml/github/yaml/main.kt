package com.github.seepick.kaml.github.yaml

import com.github.seepick.kaml.core.fixDashPlacement
import com.github.seepick.kaml.core.yamlMap
import com.github.seepick.kaml.github.domain.GithubAction

fun GithubAction.toYamlString(): String {
    val root = yamlMap()
    root.add("name", name)
    if (triggers.isNotEmpty()) {
        root.add("on", triggersYaml(triggers))
    }
    if (jobs.isNotEmpty()) {
        root.add("jobs", jobsYaml(jobs))
    }
//    val printer = Yaml.createYamlPrinter(FileWriter("/path/to/map.yml"))
//    printer.print(map)
    return root.build().toString()
        .let(::fixDashPlacement) // GitHubAction, steps mapping-sequence with "-" prefixed
        .also { println(it) }
}
