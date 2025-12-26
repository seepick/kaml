package com.github.seepick.kaml.examples.mySetup

import com.github.seepick.kaml.KamlYamlOutput
import com.github.seepick.kaml.Konfig
import com.github.seepick.kaml.ValidationLevel
import com.github.seepick.kaml.XKaml
import com.github.seepick.kaml.k8s.k8s
import com.github.seepick.kaml.kerror
import java.io.File

fun main() {
    val k8s = XKaml(Konfig(validationLevel = ValidationLevel.FailOnError)).k8s
    val backendPort = 8080
    val backendService = k8s.backendService(backendPort)
    val frontendDeployment = k8s.frontendDeployment(
        backendServiceHostAndPort =
            "${backendService.metadata.name ?: kerror("service metadata must have name")}:$backendPort"
    )
    val dbConfig = KamlConfig.toDbConfig()
    listOf(
        k8s.dbDeployment(KamlConfig.groupId, dbConfig) to "db.deployment.yaml",
        k8s.dbService(KamlConfig.groupId, dbConfig) to "db.service.yaml",
        k8s.backendDeployment() to "backend.deployment.yaml",
        backendService to "backend.service.yaml",
//        frontendDeployment to "frontend.deployment.yaml",
//        k8s.frontendService() to "frontend.service.yaml",
    ).saveAll(targetFolder = File("build/k8s"))
}

private fun List<Pair<KamlYamlOutput, String>>.saveAll(targetFolder: File) {
    if (!targetFolder.deleteRecursively()) error("Failed to delete ${targetFolder.absolutePath}")
    if (!targetFolder.mkdirs()) error("Failed to create ${targetFolder.absolutePath}")

    forEach { (manifest, fileName) ->
        val targetYamlFile = File(targetFolder, fileName)
        targetYamlFile.writeText(manifest.toYaml())
        println("Saved: ${targetYamlFile.absolutePath}")
    }
}

private fun KamlConfig.toDbConfig() = DbConfig(
    user = KamlConfig.db.userPass.first,
    pass = KamlConfig.db.userPass.second,
    port = KamlConfig.db.port,
)
