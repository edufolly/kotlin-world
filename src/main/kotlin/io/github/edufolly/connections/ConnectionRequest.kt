package io.github.edufolly.connections

/**
 * @author Eduardo Folly
 */
data class ConnectionRequest(
    val host: String,
    val port: Int,
    val user: String,
    val command: String,
)
