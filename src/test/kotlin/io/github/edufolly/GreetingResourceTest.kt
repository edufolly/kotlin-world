package io.github.edufolly

import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.common.http.TestHTTPEndpoint
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

/**
 * @author Eduardo Folly
 */
@QuarkusTest
@TestHTTPEndpoint(GreetingResource::class)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class GreetingResourceTest {

    val empty: String = "empty";

    companion object {
        @BeforeAll
        @JvmStatic
        fun initAll() {
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        }
    }

    @Test
    @Order(1)
    fun emptyCount() {
        Given {
            given()
        } When {
            queryParam("t", empty)
            get("/count")
        } Then {
            statusCode(200)
            body(Matchers.equalTo("0"))
        }
    }

    @Test
    @Order(2)
    fun firstCount() {
        When {
            get("/count")
        } Then {
            statusCode(200)
            body(Matchers.equalTo("1"))
        }
    }

    @Test
    @Order(3)
    fun emptyList() {
        Given {
            given()
        } When {
            queryParam("t", empty)
            get()
        } Then {
            statusCode(200)
            contentType(ContentType.JSON)
            body("$.size()", Matchers.equalTo(0))
        }
    }

    @Test
    @Order(4)
    fun firstList() {
        When {
            get()
        } Then {
            statusCode(200)
            contentType(ContentType.JSON)
            body("$.size()", Matchers.equalTo(1))
        }
    }

    @Test
    @Order(5)
    fun insertWithoutFields() {
        Given {
            given()
        } When {
            contentType(ContentType.JSON)
            body("{}")
            post()
        } Then {
            statusCode(400)
            contentType(ContentType.JSON)
            body(
                "errors.message", Matchers.hasItems(
                    "nameIsRequired", "descriptionIsRequired"
                )
            )
        }
    }

    @Test
    @Order(6)
    fun firstInsert() {
        Given {
            given()
        } When {
            contentType(ContentType.JSON)
            body(
                mapOf(
                    "name" to "First",
                    "description" to "First description",
                )
            )
            post()
        } Then {
            statusCode(201)
            contentType(ContentType.JSON)
        }

    }

}