package com.github.seepick.kaml.validation

import com.github.seepick.kaml.KamlException

interface DomainBuilder<D : Validatable> {
    fun build(): D
}

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

sealed interface ValidationResult {
    object Valid : ValidationResult
    data class Invalid(val issues: List<ValidationIssue>) : ValidationResult {
        companion object {
            operator fun invoke(vararg issues: ValidationIssue) = Invalid(issues.toList())
        }
    }
}

enum class ValidationSeverity(val level: ValidationLevel) {
    Warning(ValidationLevel.LogWarningsOnly),
    Error(ValidationLevel.FailOnError),
}

data class ValidationIssue(
    val message: String,
    val severity: ValidationSeverity,
)

class KamlValidationException internal constructor(
    message: String,
    val validationResult: ValidationResult.Invalid,
) : KamlException(message + "\n" + validationResult.issues.formatted)
