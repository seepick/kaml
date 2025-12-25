package com.github.seepick.kaml.k8s


import com.github.seepick.kaml.yaml.YamlMapDsl

fun YamlMapDsl.addEnv(env: Map<String, Any>) {
    if (env.isNotEmpty()) {
        seq("env") {
            env.forEach { (name, value) ->
                flatMap {
                    add("name", name)
                    add("value", value)
                }
            }
        }
    }
}
