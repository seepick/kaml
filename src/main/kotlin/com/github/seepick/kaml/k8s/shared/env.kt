package com.github.seepick.kaml.k8s.shared

import com.github.seepick.kaml.yaml.YamlMapDsl

fun YamlMapDsl.addEnv(env: Env) {
    if (env.values.isNotEmpty()) {
        seq("env") {
            env.values.forEach { (name, value) ->
                flatMap {
                    add("name", name)
                    add("value", value)
                }
            }
        }
    }
    if (env.configMapRefNames.isNotEmpty() || env.secretRefNames.isNotEmpty()) {
        seq("envFrom") {
            env.configMapRefNames.forEach { configMap ->
                flatMap {
                    map("configMapRef") {
                        add("name", configMap)
                    }
                }
            }
            env.secretRefNames.forEach { secret ->
                flatMap {
                    map("secretRef") {
                        add("name", secret)
                    }
                }
            }
        }
    }

}
