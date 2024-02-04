package io.github.edufolly.utils

import io.restassured.response.ResponseBodyExtractionOptions
import kotlin.reflect.KClass

/**
 * @author Eduardo Folly
 */
fun <T : Any> ResponseBodyExtractionOptions.parse(clazz: KClass<T>): T {
    return this.`as`(clazz.java)
}
