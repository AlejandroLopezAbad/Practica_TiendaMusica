package com.example.microserviciousuarios.dto

import com.example.microserviciousuarios.models.Users
import java.time.LocalDateTime

data class UsersDto(

    val id: Int? = null,

    val uuid:String,
    val email: String,
    val name: String,
    val telephone: String,
    val url: String,
    val rol: String,
    val metadata: MetaData? = null,
) {
    data class MetaData(
        val createdAt: LocalDateTime? = LocalDateTime.now(),
        val updatedAt: LocalDateTime? = LocalDateTime.now(),
        val deleted: Boolean = false
    )

}

data class UsersCreateDto(
    val name: String,
    val email: String,
    val rol:String,
    val password: String
)

data class UsersWithTokenDto(
    val user: UsersDto,
    val token: String
)

}

