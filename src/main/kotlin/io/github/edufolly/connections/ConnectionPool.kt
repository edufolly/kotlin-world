package io.github.edufolly.connections

/**
 * @author Eduardo Folly
 */
class ConnectionPool private constructor(private val maxConnections: Int = 10) {

    companion object {
        private var instance: ConnectionPool? = null

        fun start(maxConnections: Int = 10): ConnectionPool {
            if (instance == null) {
                instance = ConnectionPool(maxConnections = maxConnections)
                println(
                    "ConnectionPool started with " +
                            "$maxConnections maximum connections!"
                )
            } else {
                println("ConnectionPool already started!")
            }

            return instance!!
        }

        fun send(host: String, port: Int, command: String): String =
            instance!!.send(host, port, command)

        fun shutdown() {
            instance!!.shutdown()
            instance = null
        }
    }

    private val connections = ArrayList<Connection>()

    private fun send(host: String, port: Int, command: String): String {
        var connection = connections.find { it.host == host && it.port == port }

        if (connection == null) {
            connection = Connection(host, port)

            println("[$connection] Creating!")

            // TODO: Implement a connection pool with a maximum number of connections

            if (connections.size >= maxConnections) {
                connections.removeIf { !it.isConnected }

                val oldestConnection = connections.minByOrNull { it.lastPing }
                // TODO: Check if running.
                println("[$oldestConnection] Removing!")
                oldestConnection?.disconnect()
                connections.remove(oldestConnection)
            }

            connections.add(connection)
            connection.connect()
        } else {
            println("[$connection] Reusing!")
        }

        println("[$connection] Sending: $command")
        return connection.send(command)
    }

    private fun shutdown() {
        connections.forEach {
            it.disconnect()
        }
    }

}