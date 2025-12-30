package com.github.seepick.kaml.validation

fun validation(code: ValidationDsl.() -> Unit): ValidationResult {
    val issues = ValidationDsl().apply(code).issues
    return if (issues.isEmpty()) ValidationResult.Valid
    else ValidationResult.Invalid(issues)
}

class ValidationDsl {

    val issues = mutableListOf<ValidationIssue>()

    fun valid(isValid: Boolean, lazyMessage: () -> String) {
        if (!isValid) {
            issues += ValidationIssue(lazyMessage(), ValidationSeverity.Warning)
        }
    }
}
