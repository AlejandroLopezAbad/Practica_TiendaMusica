package es.tiendamusica.dtos

import kotlinx.serialization.Serializable
import serializer.LocalDateTimeSerializer
import serializer.UUIDSerializer
import java.time.LocalDateTime
import java.util.*

@Serializable
data class UserDto(
    val uuid: String,
    val email: String,
    val name: String,
    val telephone: String,
    val url: String,
    val rol: Set<String> = setOf(TypeRol.USER.name),
    val metadata: MetaData? = null
)

enum class TypeRol {
    USER, EMPLOYEE, ADMIN, SUPERADMIN
}
@Serializable

data class MetaData(
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime? = LocalDateTime.now(),
    @Serializable(with = LocalDateTimeSerializer::class)
    val updatedAt: LocalDateTime? = LocalDateTime.now(),
    val deleted: Boolean = false
)
