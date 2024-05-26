package io.github.edufolly.connections

import io.quarkus.logging.Log
import io.quarkus.scheduler.Scheduled
import jakarta.enterprise.context.ApplicationScoped

/**
 * @author Eduardo Folly
 */

@ApplicationScoped
class ConnectionScheduler {
    @Scheduled(
        identity = "removeIdleConnections",
        every = "10s",
        concurrentExecution = Scheduled.ConcurrentExecution.SKIP,
    )
    fun removeIdleConnections() {
        Log.info("Checking for idle connections...")
        ConnectionPool.removeIdleConnections()
    }
}
