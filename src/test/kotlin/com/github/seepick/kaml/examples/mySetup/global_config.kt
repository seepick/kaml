package com.github.seepick.kaml.examples.mySetup

import com.github.seepick.kaml.examples.mySetup.KamlConfig.groupId
import com.github.seepick.kaml.k8s.XK8s

// TODO make it a data class, pass through config values, make it explicit!
object KamlConfig {
    val groupId = "kaml"

    const val podLabelKey = "type"
    // should be applied to all our declarations
    val teamKamlLabel = "provider" to "team-kaml"

    val db = Db
}

object Db {
    val serviceName = "${groupId}-db-service" // maps to the hostname to be used by other pods
    val port = 5432
    // TODO configMap: https://www.digitalocean.com/community/tutorials/how-to-deploy-postgres-to-kubernetes-cluster
    // PersistentVolume
    val userPass = "postgres" to "postgres"
    val name = "${groupId}-db"
    val jdbc = "jdbc:postgresql://${serviceName}:${port}/$name"
}

val XK8s.configMap get() = KamlConfig
