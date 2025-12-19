package com.github.seepick.kaml.github.dsl

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.github.domain.Permission
import com.github.seepick.kaml.github.domain.PermissionLevel
import com.github.seepick.kaml.github.domain.PermissionType

@KamlDsl
class PermissionsDsl {
    var contents: PermissionLevel? = null
    // add more...

    fun build() = mapOf(
        PermissionType.Contents to contents,
    ).mapNotNull { entry ->
        entry.value?.let { value ->
            Permission(entry.key, value)
        }
    }
}
