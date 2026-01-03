package com.github.seepick.kaml.k8s.shared

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.kerror
import com.github.seepick.kaml.validation.invalid

data class Volume(
    val name: String,
    val mountPath: String?,
    val claim: PersistentVolumeClaim?,
)

data class PersistentVolumeClaim(
    val name: String,
)

@KamlDsl
class ClaimDsl {
    var name: String? = null
    fun build() = PersistentVolumeClaim(
        name = name ?: invalid("Claim name must be set")
    )
}

@KamlDsl
class VolumeDsl {
    var name: String? = null
    var mountPath: String? = null

    private var claim: PersistentVolumeClaim? = null
    /**
     * Set the persistent volume claim
     * See: [https://kubernetes.io/docs/concepts/storage/persistent-volumes/#claims-as-volumes](https://kubernetes.io/docs/concepts/storage/persistent-volumes/#claims-as-volumes)
     */
    fun claim(code: ClaimDsl.() -> Unit) {
        claim = ClaimDsl().apply(code).build()
    }

    fun build() = Volume(
        name = name ?: invalid("Volume name must be set"),
        mountPath = mountPath,
        claim = claim,
    )
}

data class HostPath(
    val path: String,
    val type: HostPathType,
)

@KamlDsl
class HostPathDsl {
    var path: String? = null
    var type: HostPathType? = null
    fun build() = HostPath(
        path = path ?: kerror("HostPath path must be set"),
        type = type ?: HostPathType.default,
    )
}

enum class HostPathType(val yamlValue: String) {
    Directory("Directory"),
    DirectoryOrCreate("DirectoryOrCreate"),
    File("File"),
    FileOrCreate("FileOrCreate"),
    Socket("Socket"),
    CharDevice("CharDevice"),
    BlockDevice("BlockDevice");

    companion object {
        val default = Directory
    }
}
