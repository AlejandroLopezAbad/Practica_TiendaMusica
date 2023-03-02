package com.example.microserviciousuarios.dto

import com.example.microserviciousuarios.models.Users
import java.time.LocalDateTime

data class UsersDto(
   // val id: Int? = null,
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

data class UsersCreateDto(
    val email: String,
    val name: String,
    val password: String,
    val telephone:String,
    val rol:Set<String> = setOf(Users.TypeRol.USER.name),
    val url:String?=null
)

data class UsersUpdateDto(
    val email:String,
    val name:String,

   // val telephone:String,

)


data class UsersWithTokenDto(
    val user: UsersDto,
    val token: String
)

data class UsersLoginDto(
    val email: String,
    val password: String
)



