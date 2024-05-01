package io.github.edufolly.connections

import io.quarkus.logging.Log

/**
 * @author Eduardo Folly
 */
class Connection(val host: String, val port: Int) {

    var lastPing = 0L
        private set

    var isConnected = false
        private set

    var isRunning = false
        private set

    fun connect() {
        if (isConnected) throw Exception("Already connected!")

        // Simulate a connection
        val sleep = (500L..1000L).random()
        Log.info("[$this] Sleeping for $sleep ms...")
        Thread.sleep(sleep)

        isConnected = true
        lastPing = System.currentTimeMillis()

        Log.info("[$this] Connected!")
    }

    fun send(command: String): String {
        isRunning = true
        lastPing = System.currentTimeMillis()

        // Simulate a command execution
        val sleep = (800L..2500L).random()
        Log.info("[$this] Sleeping for $sleep ms...")
        Thread.sleep(sleep)

        Log.info("[$this] Executing command: $command")

        isRunning = false
        lastPing = System.currentTimeMillis()

        return command;
    }

    fun disconnect() {
        if (!isConnected) throw Exception("Already disconnected!")

        // Simulate a disconnection
        val sleep = (500L..1000L).random()
        Log.info("[$this] Sleeping for $sleep ms...")
        Thread.sleep(sleep)

        isConnected = false
        isRunning = false

        Log.info("[$this] Disconnected!")
    }

    override fun equals(other: Any?): Boolean = if (other is Connection)
        this.hashCode() == other.hashCode() else false

    override fun hashCode(): Int = 31 * host.hashCode() + port

    override fun toString(): String = "$host:$port"
}
