package com.github.seepick.kaml.k8s.shared

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.kerror

data class Volume(
    val name: String,
    val mountPath: String?,
)

@KamlDsl
class VolumeDsl {
    var name: String? = null
    var mountPath: String? = null
    fun build() = Volume(
        name = name ?: kerror("Volume name must be set"),
        mountPath = mountPath,
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
