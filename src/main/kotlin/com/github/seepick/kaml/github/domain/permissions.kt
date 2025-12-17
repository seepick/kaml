package com.github.seepick.kaml.github.domain

data class Permission(
    val type: PermissionType,
    val level: PermissionLevel,
)

enum class PermissionType(val yamlValue: String) {
    //  actions
//  checks
    Contents("contents")
//  deployments
//  id
//  issues
//  discussions
//  packages
//  pages
//  pull
//  repository
//  security
//  statuses
}

enum class PermissionLevel(val yamlValue: String) {
    Read("read"),
    Write("write"),
    None("none"),
}
