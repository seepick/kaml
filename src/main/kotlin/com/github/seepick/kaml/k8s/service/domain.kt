package com.github.seepick.kaml.k8s.service

import com.github.seepick.kaml.k8s.K8sApiVersion
import com.github.seepick.kaml.k8s.Manifest
import com.github.seepick.kaml.k8s.ManifestKind
import com.github.seepick.kaml.k8s.Metadata
import com.github.seepick.kaml.k8s.Port

data class Service(
    override val metadata: Metadata,
    override val spec: ServiceSpec,
) : Manifest<ServiceSpec> {
    override val apiVersion = K8sApiVersion.Service
    override val kind = ManifestKind.Service
}

data class ServiceSpec(
    val type: ServiceType,
    val ports: List<Port>,
    val selector: Map<String, String>,
)

enum class ServiceType(val yamlValue: String) {
    ClusterIP("ClusterIP"),
    NodePort("NodePort"),
    LoadBalancer("LoadBalancer");

    companion object {
        val default = ClusterIP
    }
}
/*
apiVersion: v1
kind: Service
metadata:
  name: my-nginx-service
spec:
  type: ClusterIP  # or NodePort, LoadBalancer, etc.
  ports:
  # mulitple mappings can be defined here
    - protocol: TCP
      port: 80 # service port, accessible within the cluster
      targetPort: 80 # pod port; if ommitted, defaults to 'port' value
      # nodePort: 30080 # must be 30xxx by default, only for NodePort type; if ommitted, a valid port will be automatically assigned
  selector: # labels to select pods
    app: my-nginx-pods
 */