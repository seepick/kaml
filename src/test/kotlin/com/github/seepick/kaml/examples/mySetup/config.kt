package com.github.seepick.kaml.examples.mySetup

import com.github.seepick.kaml.k8s.XK8s

val groupId = "kaml"

object K8sConfigMap {
    const val podLabelKey = "type"
    val dbHostname = "${groupId}-db-host" // actually the service name
    const val dbPort = 5432
    // TODO configMap: https://www.digitalocean.com/community/tutorials/how-to-deploy-postgres-to-kubernetes-cluster
    // PersistentVolume
    val dbUserPass = "postgres" to "postgres"
    val dbName = "${groupId}-db"
    val dbJdbc = "jdbc:postgresql://${dbHostname}:${dbPort}/$dbName"
}

val XK8s.configMap get() = K8sConfigMap
