package com.github.seepick.kaml.validation

fun invalid(vararg issues: String): Nothing {
    throw KamlValidationException(
        "Invalid definition", ValidationResult.Invalid(
            issues.map { ValidationIssue(it, ValidationSeverity.Error) })
    )
}

val List<ValidationIssue>.formatted get() = joinToString("\n") { "- ${it.message}" }
