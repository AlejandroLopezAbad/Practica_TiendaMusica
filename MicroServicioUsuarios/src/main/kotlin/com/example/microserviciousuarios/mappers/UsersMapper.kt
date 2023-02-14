package com.example.microserviciousuarios.mappers

import com.example.microserviciousuarios.dto.UsersDto
import com.example.microserviciousuarios.models.Users

fun Users.toDto(): UsersDto {
    return UsersDto(
        id=this.id,
        uuid=this.uuid.toString(),
        email = this.email,
        name=this.name,
        telephone=this.telephone.toString(),
        url=this.url,
        rol = this.rol.split(",").map { it.trim() }.toSet(),
        metadata = UsersDto.MetaData(
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            deleted = this.deleted
        )
    )

}