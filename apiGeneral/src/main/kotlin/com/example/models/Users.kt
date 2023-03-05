package com.example.models


import java.time.LocalDateTime
import java.util.*

/**
 *
 *
 * @property id
 * @property uuid
 * @property email
 * @property name
 * @property password
 * @property telephone
 * @property rol
 * @property avaliable
 * @property url
 * @property createdAt
 * @property updatedAt
 * @property deleted
 */
data class Users(

    val id :Int?=null,
    val uuid:String= UUID.randomUUID().toString(),
    val email:String,
    val name:String,
    val password:String,
    val telephone:Int = 787744741,
    val rol :String=TypeRol.USER.name,
    val avaliable:Boolean=true,
    val url:String="",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val deleted: Boolean = false,
){

    enum class TypeRol() {
    USER,EMPLOYEE,ADMIN,SUPERADMIN }
}



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


