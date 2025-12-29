package com.github.seepick.kaml.examples.mySetup

import com.github.seepick.kaml.k8s.XK8s
import com.github.seepick.kaml.k8s.artifacts.ConfigMap
import com.github.seepick.kaml.k8s.artifacts.configMap

fun XK8s.mainConfigMap(): ConfigMap = configMap {
    metadata {
        name = "${appConfig.groupId}-configmap-main"
    }
    data += "DB_JDBC" to appConfig.db.jdbc
    data += "DB_USER" to appConfig.db.userPass.first
    data += "DB_PASS" to appConfig.db.userPass.second
}
