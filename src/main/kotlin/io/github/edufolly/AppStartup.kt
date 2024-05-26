package io.github.edufolly

import io.github.edufolly.connections.ConnectionPool
import io.quarkus.runtime.Shutdown
import io.quarkus.runtime.Startup
import jakarta.enterprise.context.ApplicationScoped

/**
 * @author Eduardo Folly
 */
@ApplicationScoped
class AppStartup {
    @Startup
    fun startup() {
        ConnectionPool.start(maxConnections = 10, idleTimeout = 10000)
    }

    @Shutdown
    fun shutdown() {
        ConnectionPool.shutdown()
    }
}
