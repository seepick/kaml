package com.github.seepick.kaml

import com.amihaiemil.eoyaml.Yaml
import com.amihaiemil.eoyaml.YamlMappingBuilder
import com.amihaiemil.eoyaml.YamlNode
import com.amihaiemil.eoyaml.YamlSequenceBuilder

internal fun YamlSequenceBuilder.addAllNodes(nodes: List<YamlNode>) = apply {
    nodes.forEach(::add)
}

fun YamlSequenceBuilder.addAllStrings(nodes: List<String>) = apply {
    nodes.forEach(::add)
}

fun yamlMap() = Yaml.createMutableYamlMappingBuilder()

fun yamlSeq() = Yaml.createMutableYamlSequenceBuilder()

fun yamlScal() = Yaml.createYamlScalarBuilder()

fun yamlScal(value: String) = Yaml.createYamlScalarBuilder().buildPlainScalar(value)

fun YamlMappingBuilder.addKeyValues(map: Map<String, String>) = apply {
    map.forEach { (key, value) ->
        add(key, value)
    }
}

/** Move a lone "-" line to the same line as the following mapping key.
 *  Example:
 *    "  -\n    name: X"  ->  "  - name: X"
 */
fun fixDashPlacement(yaml: String): String {
    // (?m) = multiline, ^ matches beginning of a line
    // (\\s*) captures indentation, then a '-' alone on its line, then the next line
    // starts with the same indentation plus some more spaces; we replace the matched
    // prefix with "<indent>- " so the following key remains.
    return yaml.replace(Regex("(?m)^(\\s*)-\\s*\\n\\1\\s+"), "$1- ")
}
