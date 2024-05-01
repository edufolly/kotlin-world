package io.github.edufolly

import io.github.edufolly.connections.*
import io.smallrye.common.annotation.RunOnVirtualThread
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType

/**
 * @author Eduardo Folly
 */
@Path("/connection")
@RunOnVirtualThread
class ConnectionResource {

    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun send(request: ConnectionRequest): ConnectionResult {
        val result = ConnectionPool.send(
            request.host,
            request.port,
            request.user,
            request.command
        )

        return ConnectionResult(result)
    }

}
