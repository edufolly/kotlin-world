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
        ConnectionPool.start()
    }

    @Shutdown
    fun shutdown() {
        ConnectionPool.shutdown()
    }
}