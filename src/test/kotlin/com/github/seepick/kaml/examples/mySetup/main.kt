package com.github.seepick.kaml.examples.mySetup

import com.github.seepick.kaml.Image
import com.github.seepick.kaml.KamlKonfig
import com.github.seepick.kaml.XKaml
import com.github.seepick.kaml.k8s.artifacts.deployment.Deployment
import com.github.seepick.kaml.k8s.artifacts.deployment.imageTransformer
import com.github.seepick.kaml.k8s.artifacts.deployment.replicas
import com.github.seepick.kaml.k8s.artifacts.deployment.spec
import com.github.seepick.kaml.k8s.k8s
import com.github.seepick.kaml.kerror
import com.github.seepick.kaml.saveAll
import com.github.seepick.kaml.validation.ValidationLevel
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
    val backendDeployment = k8s.backendDeployment(mainConfigMap.metadata.name!!, AppConfig.backendPort)

    // this is basically what the kustomize patch is all about:
    val backendProdDeployment = Deployment.spec.replicas.set(backendDeployment, 5)
    // or emulate a kustomize image transformer:
    val backendDeploymentWithTransformedImage = backendDeployment.imageTransformer(
        containerMatcher = { it.image == Image.demoApp },
        imageProvider = { Image.demoApp.copy(version = "1.2.3") }
    )

//    val containersLens = Deployment.spec.template.spec.containers
//    val oldContainers = containersLens.get(backendDeployment)
//    val oldContainer = oldContainers.first { it.image == Image.demoApp }
//    val newContainers = buildList {
//        addAll(oldContainers)
//        remove(oldContainer)
//        add(oldContainer.copy(image = Image.demoApp.copy(version = "1.2.3")))
//    }
//    val backendDeploymentWithTransformedImage = Deployment.spec.template.spec.containers.set(
//        backendDeployment, newContainers
//    )

    listOf(
        mainConfigMap to "main.configmap.yaml",
        k8s.dbDeployment(AppConfig.groupId, dbConfig) to "db.deployment.yaml",
        k8s.dbService(AppConfig.groupId, dbConfig) to "db.service.yaml",
        backendDeployment to "backend.deployment.yaml",
        backendService to "backend.service.yaml",
//        frontendDeployment to "frontend.deployment.yaml",
//        k8s.frontendService() to "frontend.service.yaml",
    )
//        .saveMerged(targetFile = File("build/k8s/_main_.k8s.yaml"))
        .saveAll(targetFolder = File("build/k8s"))
}


private fun AppConfig.toDbConfig() = DbConfig(
    user = AppConfig.db.userPass.first,
    pass = AppConfig.db.userPass.second,
    port = AppConfig.db.port,
)
