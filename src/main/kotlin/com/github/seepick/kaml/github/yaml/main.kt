package com.github.seepick.kaml.github.yaml

import com.github.seepick.kaml.github.domain.GithubAction
import com.github.seepick.kaml.yaml.toCleanYamlString
import com.github.seepick.kaml.yaml.yamlMap

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
    return root.build().toCleanYamlString()
}
