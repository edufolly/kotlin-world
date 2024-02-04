package io.github.edufolly

import io.github.edufolly.entities.MyKotlinEntity
import io.github.edufolly.utils.parse
import io.quarkus.test.common.http.TestHTTPEndpoint
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.*
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.*

/**
 * @author Eduardo Folly
 */
@QuarkusTest
@TestHTTPEndpoint(GreetingResource::class)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class GreetingResourceTest {

    companion object {

        const val EMPTY_SEARCH_TERM: String = "empty"
        const val WRONG_ID: Int = 0
        val entityMap = hashMapOf<String, Any>()
        var count: Int = 1

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
            queryParam("t", EMPTY_SEARCH_TERM)
            get("/count")
        } Then {
            statusCode(200)
            body(equalTo("0"))
        }
    }

    @Test
    @Order(2)
    fun firstCount() {
        When {
            get("/count")
        } Then {
            statusCode(200)
            body(equalTo(count.toString()))
        }
    }

    @Test
    @Order(3)
    fun emptyList() {
        Given {
            given()
        } When {
            queryParam("t", EMPTY_SEARCH_TERM)
            get()
        } Then {
            statusCode(200)
            contentType(ContentType.JSON)
            body("$.size()", equalTo(0))
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
            body("$.size()", equalTo(count))
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
                "errors.message", hasItems(
                    "nameIsRequired",
                    "descriptionIsRequired"
                )
            )
        }
    }

    @Test
    @Order(6)
    fun firstInsert() {
        entityMap["name"] = "First Name Test"
        entityMap["description"] = "First Description Test"

        val entity: MyKotlinEntity = Given {
            given()
        } When {
            contentType(ContentType.JSON)
            body(entityMap)
            post()
        } Then {
            statusCode(201)
            contentType(ContentType.JSON)
            body(
                "name", equalTo(entityMap["name"]),
                "description", equalTo(entityMap["description"]),
                "updatedAt", notNullValue(),
            )
        } Extract {
            body().parse(MyKotlinEntity::class)
        }

        count++
        entityMap["id"] = entity.id!!.toInt()
        entityMap["updatedAt"] = entity.updatedAt.time
    }

    @Test
    @Order(7)
    fun firstGet() {
        Given {
            given()
        } When {
            pathParam("id", entityMap["id"])
            get("/{id}")
        } Then {
            statusCode(200)
            contentType(ContentType.JSON)
            body("$", equalTo(entityMap))
        }
    }

    @Test
    @Order(8)
    fun secondCount() {
        When {
            get("/count")
        } Then {
            statusCode(200)
            body(equalTo(count.toString()))
        }
    }

    @Test
    @Order(9)
    fun secondList() {
        Given {
            given()
        } When {
            queryParam("t", "test")
            get()
        } Then {
            statusCode(200)
            contentType(ContentType.JSON)
            body(
                "$.size()", equalTo(1),
                "[0]", equalTo(entityMap)
            )
        }
    }

    @Test
    @Order(10)
    fun getWithWrongId() {
        Given {
            given()
        } When {
            pathParam("id", WRONG_ID)
            get("/{id}")
        } Then {
            statusCode(404)
            contentType(ContentType.JSON)
            body(
                "errors.message", hasItems(
                    "entityNotFound"
                )
            )
        }
    }
}
