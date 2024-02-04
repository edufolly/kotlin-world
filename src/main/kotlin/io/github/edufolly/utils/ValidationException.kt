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
    class forbidden :
        ValidationException {
        constructor(vararg errors: ValidationError) : super(
            Response.Status.FORBIDDEN,
            *errors
        )

        constructor(
            type: ValidationErrorType,
            path: String,
            message: String,
            value: Any?
        ) : this(ValidationError(type, path, message, value))
    }


    class notFound :
        ValidationException {
        constructor(vararg errors: ValidationError) : super(
            Response.Status.NOT_FOUND,
            *errors
        )

        constructor(
            type: ValidationErrorType,
            path: String,
            message: String,
            value: Any?
        ) : this(ValidationError(type, path, message, value))
    }

    class badRequest :
        ValidationException {
        constructor(vararg errors: ValidationError) : super(
            Response.Status.BAD_REQUEST,
            *errors
        )

        constructor(
            type: ValidationErrorType,
            path: String,
            message: String,
            value: Any?
        ) : this(ValidationError(type, path, message, value))
    }
}