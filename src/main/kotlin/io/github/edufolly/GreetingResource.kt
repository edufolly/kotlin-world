package io.github.edufolly

import io.github.edufolly.entities.MyKotlinEntity
import io.github.edufolly.utils.ValidationErrorType
import io.github.edufolly.utils.ValidationException
import jakarta.transaction.Transactional
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.DefaultValue
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.jboss.resteasy.reactive.RestPath
import org.jboss.resteasy.reactive.RestQuery
import java.net.URI
import java.util.*

/**
 * @author Eduardo Folly
 */
@Path("/hello")
class GreetingResource {

    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    fun count(@RestQuery @DefaultValue("") t: String): String =
        MyKotlinEntity.search(t).count().toString()

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun list(
        @RestQuery @DefaultValue("") t: String,
        @RestQuery @DefaultValue("0") page: Int,
        @RestQuery @DefaultValue("20") size: Int
    ): List<MyKotlinEntity> = MyKotlinEntity.search(t).page(page, size).list()

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun findById(id: Long): MyKotlinEntity =
        MyKotlinEntity.findById(id) ?: throw ValidationException.NotFound(
            ValidationErrorType.ENTITY,
            "entityNotFound", "id", id
        )

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun add(entity: MyKotlinEntity): Response {
        entity.validate().persist()
        return Response.created(path(entity.id!!)).entity(entity).build()
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun update(id: Long, entity: MyKotlinEntity): Response =
        MyKotlinEntity.findById(id)?.run {
            entity.validate()
            name = entity.name
            description = entity.description
            updatedAt = Date()
            persist()
            Response.ok(this).location(path(this.id!!)).build()
        } ?: throw ValidationException.NotFound(
            ValidationErrorType.ENTITY,
            "entityNotFound", "id", id
        )

    @DELETE
    @Path("/{id}")
    @Transactional
    fun delete(@RestPath id: Long): Response =
        MyKotlinEntity.findById(id)?.run {
            delete()
            Response.noContent().build()
        } ?: throw ValidationException.NotFound(
            ValidationErrorType.ENTITY,
            "entityNotFound", "id", id
        )

    private fun path(id: Long): URI = URI.create("/hello/$id")
}