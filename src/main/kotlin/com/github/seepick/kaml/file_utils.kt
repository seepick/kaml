package com.github.seepick.kaml

import java.io.File

fun List<Pair<KamlYamlOutput, String>>.saveMerged(targetFile: File) {
    val yaml = joinToString("\n---\n") { (manifest, yamlName) -> "# ${yamlName}\n" + manifest.toYaml() }
    targetFile.writeText(yaml)
    println(
        "Merged manifests saved to: ${targetFile.absolutePath}\n" +
                "You can now apply them with: `kubectl apply -f ${targetFile.absolutePath}`"
    )
}

fun List<Pair<KamlYamlOutput, String>>.saveAll(targetFolder: File) {
    if (!targetFolder.deleteRecursively()) error("Failed to delete ${targetFolder.absolutePath}")
    if (!targetFolder.mkdirs()) error("Failed to create ${targetFolder.absolutePath}")

    forEach { (manifest, fileName) ->
        val targetYamlFile = File(targetFolder, fileName)
        targetYamlFile.writeText(manifest.toYaml())
        println("Saved: ${targetYamlFile.absolutePath}")
    }
    println(
        "All manifests successfully saved.\n" +
                "You can now apply them with: `kubectl apply -R -f ${targetFolder.absolutePath}` (--dry-run=client)"
    ) // TODO doesn't it require a specific order?!
}