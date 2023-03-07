package com.example.microserviciousuarios.dto

import com.example.microserviciousuarios.models.Users
import java.time.LocalDateTime


/**
 * Dto de usuario, añade usuarios a la API.
 */
data class UsersDto(
    val uuid:String,
    val email: String,
    val name: String,
    val telephone: String,
    val url: String,
    val rol: Set<String> = setOf(Users.TypeRol.USER.name),
    val metadata: MetaData? = null,
) {
    data class MetaData(
        val createdAt: LocalDateTime? = LocalDateTime.now(),
        val updatedAt: LocalDateTime? = LocalDateTime.now(),
        val deleted: Boolean = false
    )

}
/**
 * Dto que crea un usuario.
 */
data class UsersCreateDto(
    val email: String,
    val name: String,
    val password: String,
    val telephone:String,
    val rol:Set<String> = setOf(Users.TypeRol.USER.name),
    val url:String?=null
)
/**
 * Dto que actualiza un usuario.
 */
data class UsersUpdateDto(
    val email:String,
    val name:String,
    val telephone:String,
)
/**
 * Dto para el token de usuario.
 */
data class UsersWithTokenDto(
    val user: UsersDto,
    val token: String
)
/**
 * dto para el login de usuario.
 */
data class UsersLoginDto(
    val email: String,
    val password: String
)



