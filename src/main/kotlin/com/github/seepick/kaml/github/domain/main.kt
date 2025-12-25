package com.github.seepick.kaml.github.domain

import com.amihaiemil.eoyaml.YamlNode
import com.github.seepick.kaml.KamlYamlOutput
import com.github.seepick.kaml.github.yaml.jobsYaml
import com.github.seepick.kaml.github.yaml.triggersYaml
import com.github.seepick.kaml.yaml.yamlMap

data class GithubAction(
    val name: String,
    val triggers: List<Trigger>,
    val jobs: List<Job>,
) : KamlYamlOutput {
    //  TODO add checker logic here; wrapped around for all KamlDsls
    init {
//        require(name.isNotEmpty()) { "Name must not be empty!" }
//        require(triggers.isNotEmpty()) { "At least 1 trigger is required!" }
//        require(jobs.isNotEmpty()) { "At least 1 job is required!" }
    }

    override fun toYamlNode(): YamlNode {
        // TODO use new yaml DSL
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
        return root.build()
    }
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
