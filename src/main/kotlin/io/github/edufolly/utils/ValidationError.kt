package io.github.edufolly.utils

import io.quarkus.runtime.annotations.RegisterForReflection

/**
 * @author Eduardo Folly
 */
@RegisterForReflection
class ValidationError {
    val type: ValidationErrorType
    val message: String
    val field: String
    val value: String

    constructor(
        type: ValidationErrorType,
        message: String,
        field: String,
    ) : this(type, message, field, null)

    constructor(
        type: ValidationErrorType,
        message: String,
        field: String,
        value: Any?
    ) {
        this.type = type
        this.message = message
        this.field = field
        this.value = value?.toString() ?: "[null]"
    }

}





