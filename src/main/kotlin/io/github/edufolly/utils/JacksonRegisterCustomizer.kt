package io.github.edufolly.utils

import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import io.quarkus.jackson.ObjectMapperCustomizer
import jakarta.enterprise.context.ApplicationScoped
import java.util.*

/**
 * @author Eduardo Folly
 */
@ApplicationScoped
class JacksonRegisterCustomizer : ObjectMapperCustomizer {
    override fun customize(objectMapper: ObjectMapper) {
        val dateModule = SimpleModule()

        dateModule.addSerializer(
            Date::class.java,
            JacksonDateSerializer(),
        )

        dateModule.addDeserializer(
            Date::class.java,
            JacksonDateDeserializer(),
        )

        objectMapper.registerModule(dateModule)
    }
}
