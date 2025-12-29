package com.github.seepick.kaml.examples.mySetup

import com.github.seepick.kaml.KamlKonfig
import com.github.seepick.kaml.KamlYamlOutput
import com.github.seepick.kaml.ValidationLevel
import com.github.seepick.kaml.XKaml
import com.github.seepick.kaml.k8s.k8s
import com.github.seepick.kaml.kerror
import java.io.File

fun main() {
    val k8s = XKaml(KamlKonfig(validationLevel = ValidationLevel.FailOnError)).k8s
    val backendService = k8s.backendService(AppConfig.backendPort)
    val frontendDeployment = k8s.frontendDeployment(
        backendServiceHostAndPort =
            "${backendService.metadata.name ?: kerror("service metadata must have name")}:${AppConfig.backendPort}"
    )
    val dbConfig = AppConfig.toDbConfig()
    val mainConfigMap = k8s.mainConfigMap()
    listOf(
        mainConfigMap to "main.configmap.yaml",
        k8s.dbDeployment(AppConfig.groupId, dbConfig) to "db.deployment.yaml",
        k8s.dbService(AppConfig.groupId, dbConfig) to "db.service.yaml",
        k8s.backendDeployment(mainConfigMap.metadata.name!!, AppConfig.backendPort) to "backend.deployment.yaml",
        backendService to "backend.service.yaml",
//        frontendDeployment to "frontend.deployment.yaml",
//        k8s.frontendService() to "frontend.service.yaml",
    )
        .saveMerged(targetFile = File("build/k8s/_main_.k8s.yaml"))
    //.saveAll(targetFolder = File("build/k8s"))
}

private fun List<Pair<KamlYamlOutput, String>>.saveMerged(targetFile: File) {
    val yaml = joinToString("\n---\n") { (manifest, yamlName) -> "# ${yamlName}\n" + manifest.toYaml() }
    targetFile.writeText(yaml)
    println(
        "Merged manifests saved to: ${targetFile.absolutePath}\n" +
                "You can now apply them with: `kubectl apply -f ${targetFile.absolutePath}`"
    )
}

private fun List<Pair<KamlYamlOutput, String>>.saveAll(targetFolder: File) {
    if (!targetFolder.deleteRecursively()) error("Failed to delete ${targetFolder.absolutePath}")
    if (!targetFolder.mkdirs()) error("Failed to create ${targetFolder.absolutePath}")

    forEach { (manifest, fileName) ->
        val targetYamlFile = File(targetFolder, fileName)
        targetYamlFile.writeText(manifest.toYaml())
        println("Saved: ${targetYamlFile.absolutePath}")
    }
    println(
        "All manifests successfully saved.\n" +
                "You can now apply them with: `kubectl apply -R -f ${targetFolder.absolutePath}`"
    ) // TODO doesn't it require a specific order?!
}

private fun AppConfig.toDbConfig() = DbConfig(
    user = AppConfig.db.userPass.first,
    pass = AppConfig.db.userPass.second,
    port = AppConfig.db.port,
)
