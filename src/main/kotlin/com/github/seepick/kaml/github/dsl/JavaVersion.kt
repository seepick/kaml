package com.github.seepick.kaml.github.dsl

@ConsistentCopyVisibility
data class JavaVersion private constructor(val yamlValue: String) {
    companion object {
        val v17 = JavaVersion("17")

        fun parse(string: String): JavaVersion {
            // TODO run some checks...
            return JavaVersion(string)
        }
    }
}
