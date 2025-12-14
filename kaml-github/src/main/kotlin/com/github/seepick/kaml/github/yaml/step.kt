package com.github.seepick.kaml.github.yaml

import com.amihaiemil.eoyaml.YamlMapping
import com.github.seepick.kaml.core.addKeyValues
import com.github.seepick.kaml.core.yamlMap
import com.github.seepick.kaml.github.domain.GenericStep
import com.github.seepick.kaml.github.domain.RunStep
import com.github.seepick.kaml.github.domain.Step

internal fun stepYaml(step: Step): YamlMapping {
    val stepYaml = yamlMap().add("name", step.name)
    step.uses?.let { uses ->
        stepYaml.add("uses", uses.coordinates)
    }
    when (step) {
        is GenericStep -> {
            if (step.withParams.isNotEmpty()) {
                stepYaml.add("with", yamlMap().addKeyValues(step.withParams).build())
            }
        }

        is RunStep -> {
            stepYaml.add("run", step.command)
            // TODO support multi-line string properly
//                    .addLine("line1")
//                    .addLine("line2")
//                    .buildLiteralBlockScalar("block comment")
        }
    }
    return stepYaml.build()
}
