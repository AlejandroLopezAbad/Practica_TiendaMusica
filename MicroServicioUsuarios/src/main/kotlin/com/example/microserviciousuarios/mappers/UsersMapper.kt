package com.example.microserviciousuarios.mappers

import com.example.microserviciousuarios.dto.UsersCreateDto
import com.example.microserviciousuarios.dto.UsersDto
import com.example.microserviciousuarios.models.Users

/**
 * Mapper de User a UserDto.
 */
fun Users.toDto(): UsersDto {

    return UsersDto(

        uuid = this.uuid,
        email = this.email,
        name = this.name,
        telephone = this.telephone.toString(),
        url = this.url,
        rol =  this.rol.split(",").map { it.trim() }.toSet(),
        metadata = UsersDto.MetaData(
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            deleted = this.deleted
        )
    )

}


/**
 * Mapper de UsersCreateDto al modelo de Users.
 */
fun UsersCreateDto.toModel(): Users {
    return Users(
        email = this.email,
        name = this.name,
        password = this.password,
        telephone = this.telephone.toInt(),
        rol =  this.rol.joinToString(", ") { it.uppercase().trim() },
    )
}