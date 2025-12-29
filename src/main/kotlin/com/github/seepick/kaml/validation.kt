package com.github.seepick.kaml

import io.github.oshai.kotlinlogging.KotlinLogging

private val log = KotlinLogging.logger {}

enum class ValidationLevel {
    Disabled,
    LogWarningsOnly,
    /** throws [KamlValidationException] */
    FailOnError;

    companion object {
        val default = Disabled
    }
}

interface Validatable {
    fun validate(): ValidationResult
}

val List<ValidationIssue>.formatted get() = joinToString("\n") { "- ${it.message}" }

sealed interface ValidationResult {
    object Valid : ValidationResult
    data class Invalid(val issues: List<ValidationIssue>) : ValidationResult
}

data class ValidationIssue(val message: String)

class KamlValidationException internal constructor(
    message: String,
    val validationResult: ValidationResult.Invalid,
) : KamlException(message + "\n" + validationResult.issues.formatted)

class ValidationDsl {
    val issues = mutableListOf<ValidationIssue>()
    fun check(isValid: Boolean, message: String) {
        if (!isValid) {
            issues += ValidationIssue(message)
        }
    }

    fun mergeWith(validatable: Validatable) {
        when (val result = validatable.validate()) {
            is ValidationResult.Invalid -> issues += result.issues
            ValidationResult.Valid -> { /* no-op */
            }
        }
    }
}

fun validation(code: ValidationDsl.() -> Unit): ValidationResult {
    val issues = ValidationDsl().apply(code).issues
    return if (issues.isEmpty()) ValidationResult.Valid
    else ValidationResult.Invalid(issues)
}

fun <D> handleValidation(konfig: KamlKonfig, domain: D, vararg results: ValidationResult): D {
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

