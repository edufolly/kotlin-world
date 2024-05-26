package io.github.edufolly.connections

import io.quarkus.logging.Log

/**
 * @author Eduardo Folly
 */
class ConnectionPool private constructor(
    private val maxConnections: Int = 30,
    private val idleTimeout: Long = 180000, // 3 min
) {
    companion object {
        private var instance: ConnectionPool? = null

        fun start(
            maxConnections: Int = 30,
            idleTimeout: Long = 180000,
        ): ConnectionPool {
            if (instance == null) {
                instance =
                    ConnectionPool(
                        maxConnections = maxConnections,
                        idleTimeout = idleTimeout,
                    )
                Log.info(
                    "ConnectionPool started with " +
                        "$maxConnections maximum connections and " +
                        "${idleTimeout}ms for idle timeout!",
                )
            } else {
                Log.info("ConnectionPool already started!")
            }

            return instance!!
        }

        fun send(
            host: String,
            port: Int,
            user: String,
            command: String,
        ): String =
            instance?.send(host, port, user, command)
                ?: throw Exception("ConnectionPool not started!")

        fun countConnections(): Int = instance?.connections?.size ?: 0

        fun removeIdleConnections() = instance?.removeIdleConnections()

        fun shutdown() {
            instance?.shutdown()
            instance = null
        }
    }

    private val connections = ArrayList<Connection>()

    private fun send(
        host: String,
        port: Int,
        user: String,
        command: String,
    ): String {
        var connection =
            connections.find {
                it.host == host && it.port == port && it.user == user
            }

        if (connection == null) {
            connection = Connection(host, port, user)

            Log.info("[$connection] Creating!")

            if (connections.size >= maxConnections) {
                connections.removeIf { !it.isConnected }

                val oldestConnection: Connection? =
                    connections
                        .filter { !it.isRunning }
                        .minByOrNull { it.lastPing }

                if (oldestConnection == null) {
                    throw Exception("No connections available!")
                } else {
                    Log.info("[$oldestConnection] Removing!")
                    oldestConnection.disconnect()
                    connections.remove(oldestConnection)
                }
            }

            connections.add(connection)
            connection.connect()
        } else {
            Log.info("[$connection] Reusing!")
        }

        Log.info("[$connection] Sending: $command")

        return connection.send(command)
    }

    private fun removeIdleConnections() {
        connections.removeIf {
            if (!it.isRunning &&
                (System.currentTimeMillis() - it.lastPing) > idleTimeout
            ) {
                Log.info("[$it] Removing due to inactivity!")
                it.disconnect()
                true
            } else {
                false
            }
        }
    }

    private fun shutdown() {
        connections.forEach {
            it.disconnect()
        }
    }
}
