package io.github.edufolly.entities

import io.github.edufolly.utils.ValidationError
import io.github.edufolly.utils.ValidationErrorType
import io.github.edufolly.utils.ValidationException
import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery
import io.quarkus.panache.common.Sort
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

/**
 * @author Eduardo Folly
 */
@Entity
@Table(name = "my_kotlin_entity")
@SQLDelete(sql = "UPDATE my_kotlin_entity SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at = '1970-01-01 00:00:00+00:00'")
class MyKotlinEntity : AbstractFullEntity() {
    companion object : PanacheCompanion<MyKotlinEntity> {
        fun search(
            term: String,
            sort: Sort = Sort.ascending("name")
        ): PanacheQuery<MyKotlinEntity> = if (term.isEmpty()) {
            findAll(sort)
        } else {
            find(
                "LOWER(name) LIKE ?1 OR LOWER(description) LIKE ?1",
                sort, "%$term%"
            )
        }
    }

    @Column(name = "name", nullable = false, length = 40)
    lateinit var name: String

    @Column(name = "description", nullable = false, length = 600)
    lateinit var description: String

    override fun validate(): MyKotlinEntity {
        val errors = mutableListOf<ValidationError>()

        if (!this::name.isInitialized || name.isBlank()) {
            errors.add(
                ValidationError(
                    ValidationErrorType.FIELD,
                    "nameIsRequired", "name"
                )
            )
        }

        if (!this::description.isInitialized || description.isBlank()) {
            errors.add(
                ValidationError(
                    ValidationErrorType.FIELD,
                    "descriptionIsRequired", "description"
                )
            )
        }

        if (errors.isNotEmpty()) {
            throw ValidationException.BadRequest(*errors.toTypedArray())
        }

        return this
    }

}
