package com.github.seepick.kaml.validation

import com.github.seepick.kaml.KamlKonfig
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.reflect.KClass
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubtypeOf

private val log = KotlinLogging.logger {}

fun <D : Validatable> handleValidation(konfig: KamlKonfig, domain: D): D {
    val results = scanValidatables(domain)
    val issues = results.filterIsInstance<ValidationResult.Invalid>().flatMap { it.issues }
    return if (issues.isEmpty()) domain
    else {
        when (konfig.validationLevel) {
            ValidationLevel.Disabled -> domain

            ValidationLevel.LogWarningsOnly -> {
                log.warn { "Validation failed!\n" + issues.formatted }
                domain
            }

            ValidationLevel.FailOnError -> throw KamlValidationException(
                "Validation failed!",
                ValidationResult.Invalid(issues)
            )
        }
    }
}


private fun scanValidatables(root: Any): List<ValidationResult> {
//    println("checking root ... $root")
    return buildList {
        if (root is Validatable) {
//            println("  is validatable!")
            add(root.validate())
        }
        root::class.declaredMemberProperties
            .forEach { prop ->
//                println("  [${prop.name}] -> [${prop.returnType}]")
                val x = prop.returnType.classifier as KClass<*>
                if (prop.returnType.isMarkedNullable && prop.call(root) == null) {
//                    println("  skip null")
                } else if (x.qualifiedName!!.startsWith("com.github.seepick.kaml")) {
//                    println("    recursive call for: ${prop.name}")
                    addAll(scanValidatables(prop.call(root)!!))
                } else if (prop.returnType.isSubtypeOf(
                        List::class.createType(
                            arguments = listOf(KTypeProjection.invariant(Any::class.createType()))
                        )
                    )
                ) {
                    addAll((prop.call(root) as List<*>).flatMap { scanValidatables(it!!) })
                }
                // TODO handle map
            }
    }
}
