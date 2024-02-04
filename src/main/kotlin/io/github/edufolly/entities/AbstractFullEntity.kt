package io.github.edufolly.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import java.util.*

/**
 * @author Eduardo Folly
 */
@MappedSuperclass
abstract class AbstractFullEntity : AbstractEntity() {

    @JsonIgnore
    @Column(
        name = "created_at",
        nullable = false,
        updatable = false,
        columnDefinition = "timestamp with time zone not null"
    )
    var createdAt: Date = Date()

    @Column(
        name = "updated_at",
        nullable = false,
        columnDefinition = "timestamp with time zone not null"
    )
    var updatedAt: Date = Date()

    @JsonIgnore
    @Column(
        name = "deleted_at",
        nullable = false,
        columnDefinition = "timestamp with time zone not null"
    )
    var deletedAt: Date = Date(0)
}