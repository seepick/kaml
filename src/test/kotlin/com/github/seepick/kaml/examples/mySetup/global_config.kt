package com.github.seepick.kaml.examples.mySetup

import com.github.seepick.kaml.examples.mySetup.AppConfig.groupId
import com.github.seepick.kaml.k8s.XK8s

// TODO make it a data class, pass through config values, make it explicit!
object AppConfig {
    val groupId = "kaml"
    val backendPort = 8080

    val db = Db
    val labels = Labels
}

object Labels {
    val podLabelKey = "type"
    // should be applied to all our declarations
    val teamKamlLabel = "provider" to "team-kaml"
}

object Db {
    val serviceName = "${groupId}-db-service" // maps to the hostname to be used by other pods
    val port = 5432
    // TODO configMap: https://www.digitalocean.com/community/tutorials/how-to-deploy-postgres-to-kubernetes-cluster
    // PersistentVolume
    val userPass = "postgres" to "postgres"
    val dbName = "${groupId}-dbName"
    val jdbc = "jdbc:postgresql://${serviceName}:${port}/$dbName"
}

val XK8s.appConfig get() = AppConfig
