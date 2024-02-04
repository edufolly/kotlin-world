package io.github.edufolly.utils

import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response

/**
 * @author Eduardo Folly
 */
sealed class ValidationException(
    status: Response.Status,
    vararg errors: ValidationError
) : WebApplicationException(
    Response.status(status).entity(mapOf("errors" to errors)).build()
) {
    class Forbidden :
        ValidationException {
        constructor(vararg errors: ValidationError) : super(
            Response.Status.FORBIDDEN,
            *errors
        )

        constructor(
            type: ValidationErrorType,
            message: String,
            field: String,
            value: Any?
        ) : this(ValidationError(type, message, field, value))
    }

    class NotFound :
        ValidationException {
        constructor(vararg errors: ValidationError) : super(
            Response.Status.NOT_FOUND,
            *errors
        )

        constructor(
            type: ValidationErrorType,
            message: String,
            field: String,
            value: Any?
        ) : this(ValidationError(type, message, field, value))
    }

    class BadRequest :
        ValidationException {
        constructor(vararg errors: ValidationError) : super(
            Response.Status.BAD_REQUEST,
            *errors
        )

        constructor(
            type: ValidationErrorType,
            message: String,
            field: String,
            value: Any?
        ) : this(ValidationError(type, message, field, value))
    }
}