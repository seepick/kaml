package com.github.seepick.kaml.examples.mySetup

import com.github.seepick.kaml.Konfig
import com.github.seepick.kaml.ValidationLevel
import com.github.seepick.kaml.XKaml
import com.github.seepick.kaml.k8s.XK8s
import com.github.seepick.kaml.k8s.k8s
import java.io.File

val groupId = "kaml"

object K8sConfigMap {
    val podLabelKey = "type"
    val dbHostname = "${groupId}-db-host"
    //    val dbServiceName = "my-db-service" // equals hostname
    val postgresCreds = "postgres" to "postgres"
}

val XK8s.configMap get() = K8sConfigMap

fun main() {
    val k8s = XKaml(Konfig(validationLevel = ValidationLevel.FailOnError)).k8s

    val rootFolder = File("build/k8s")
    rootFolder.mkdirs()
    println("Saving to: ${rootFolder.absolutePath}")

    listOf(
        k8s.backendDeployment(groupId) to "app.deployment.yaml",
        k8s.backendService(groupId) to "app.service.yaml",
//        k8s.dbDeployment(
//            prefix, DbDeploymentConfig(
//                postgresUser = K8sConfigMap.postgresCreds.first,
//                postgresPass = K8sConfigMap.postgresCreds.second,
//            )
//        ) to "db.deployment.yaml",
    ).forEach { (manifest, fileName) ->
        val target = File(rootFolder, fileName)
        target.writeText(manifest.toYaml())
        println("Saved: ${target.absolutePath}")
    }
}