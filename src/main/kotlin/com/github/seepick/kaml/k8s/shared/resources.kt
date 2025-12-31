package com.github.seepick.kaml.k8s.shared

import com.github.seepick.kaml.KamlDsl
import com.github.seepick.kaml.validation.Validatable
import com.github.seepick.kaml.validation.validation
import com.github.seepick.kaml.yaml.YamlMapDsl

data class Resources(
    val requests: ResourceSpec?,
    val limits: ResourceSpec?,
) {
    init {
        require(requests != null || limits != null) { "At least one resource spec must be specified" }
    }
}

data class ResourceSpec(
    val cpu: Cpu?,
    val memory: Memory?,
) : Validatable {
    override fun validate() = validation {
        valid(cpu != null || memory != null) { "At least one resource must be specified" }
    }
}

@KamlDsl
class ResourcesDsl {

    var requests: ResourceSpecDsl? = null
    var limits: ResourceSpecDsl? = null

    fun requests(code: ResourceSpecDsl.() -> Unit) {
        requests = ResourceSpecDsl().apply(code)
    }

    fun limits(code: ResourceSpecDsl.() -> Unit) {
        limits = ResourceSpecDsl().apply(code)
    }

    internal fun build() = Resources(
        requests = requests?.build(),
        limits = limits?.build(),
    )
}

@KamlDsl
class ResourceSpecDsl {
    var memory: Memory? = null
    var cpu: Cpu? = null
    fun build() = if (memory == null && cpu == null) null else ResourceSpec(
        memory = memory,
        cpu = cpu,
    )
}

sealed interface Cpu {
    data class IntCpu(val value: Int) : Cpu {
        init {
            require(value >= 1) { "CPU must be at least 1 core" }
        }

        override fun toYaml() = value.toString()
    }

    data class DoubleCpu(val value: Double) : Cpu {
        init {
            // 0.1 == 100m (milli)
            require(value >= 0.1) { "CPU must be at least 0.1" }
        }

        override fun toYaml() = value.toString()
    }

    data class MilliCpu(val value: Int) : Cpu {
        init {
            require(value >= 1) { "CPU must be at least 1 milli-core" }
        }

        override fun toYaml() = "${value.toString()}$SYMBOL"

        companion object {
            const val SYMBOL = "m"
            val minimum = MilliCpu(1)
        }
    }

    fun toYaml(): String
}

val Int.cpu get() = Cpu.IntCpu(this)
val Double.cpu get() = Cpu.DoubleCpu(this)
val Int.milliCpu get() = Cpu.MilliCpu(this)

data class Memory(val value: Int, val size: MemorySize) {
    fun toYaml() = "$value${size.yamlSymbol}"
}

enum class MemorySize(val yamlSymbol: String) {
    MibiByte("Mi"),
    GibiByte("Gi"),
    TibiByte("Ti"),
}

// Gibi-byte is 1024, opposed to 1000 with Giga-byte
val Int.Mi get() = Memory(this, MemorySize.MibiByte)
val Int.Gi get() = Memory(this, MemorySize.GibiByte)
val Int.Ti get() = Memory(this, MemorySize.TibiByte)


fun YamlMapDsl.addResources(resources: Resources?) {
    if (resources == null) return
    map("resources") {
        addResourceSpec("requests", resources.requests)
        addResourceSpec("limits", resources.limits)
    }
}

fun YamlMapDsl.addResourceSpec(name: String, resource: ResourceSpec?) {
    if (resource != null) {
        map(name) {
            addIfNotNull("cpu", resource.cpu?.toYaml())
            addIfNotNull("memory", resource.memory?.toYaml())
        }
    }
}
