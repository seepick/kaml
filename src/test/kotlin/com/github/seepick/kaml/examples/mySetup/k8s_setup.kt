package com.github.seepick.kaml.examples.mySetup

import com.github.seepick.kaml.Konfig
import com.github.seepick.kaml.ValidationLevel
import com.github.seepick.kaml.XKaml
import com.github.seepick.kaml.k8s.k8s
import java.io.File

fun main() {
    val k8s = XKaml(Konfig(validationLevel = ValidationLevel.FailOnError)).k8s

    val rootFolder = File("build/k8s")
    if (!rootFolder.deleteRecursively()) error("Failed to delete ${rootFolder.absolutePath}")
    if (!rootFolder.mkdirs()) error("Failed to create ${rootFolder.absolutePath}")

    listOf(
        k8s.backendDeployment(groupId) to "app.deployment.yaml",
        k8s.backendService(groupId) to "app.service.yaml",
//        k8s.dbDeployment(groupId, K8sConfigMap.toDbDeploymentConfig()) to "db.deployment.yaml",
//        k8s.dbService(groupId) to "db.service.yaml",
    ).forEach { (manifest, fileName) ->
        val target = File(rootFolder, fileName)
        target.writeText(manifest.toYaml())
        println("Saved: ${target.absolutePath}")
    }
}

private fun K8sConfigMap.toDbDeploymentConfig() = DbDeploymentConfig(
    user = K8sConfigMap.dbUserPass.first,
    pass = K8sConfigMap.dbUserPass.second,
    port = K8sConfigMap.dbPort,
)
