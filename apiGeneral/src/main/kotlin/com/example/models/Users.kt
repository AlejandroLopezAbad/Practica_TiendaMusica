package com.example.models

import com.example.utils.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

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

@Serializable

data class MetaData(
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime? = LocalDateTime.now(),
    @Serializable(with = LocalDateTimeSerializer::class)
    val updatedAt: LocalDateTime? = LocalDateTime.now(),
    val deleted: Boolean = false
)

@Serializable
data class UserLoginDto(
    val email:String,
    val password:String
)
@Serializable

data class UserCreateDto(
    val email: String,
    val name: String,
    val password: String,
    val telephone: String,
    val url: String,
    val rol: Set<String> = setOf(TypeRol.USER.name)
)

@Serializable

data class UserTokenDto(
    val user: UserDto,
    val token: String
)

enum class TypeRol {
    USER, EMPLOYEE, ADMIN, SUPERADMIN
}