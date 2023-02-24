package com.example.microserviciousuarios.mappers


import com.example.microserviciousuarios.dto.UsersCreateDto

import com.example.microserviciousuarios.dto.UsersDto
import com.example.microserviciousuarios.models.Users

fun Users.toDto(): UsersDto {
    return UsersDto(
      //  id=this.id,
        uuid=this.uuid,
        email = this.email,
        name=this.name,
        telephone=this.telephone.toString(),
        url=this.url,
        rol = rol.name, //this.rol.split(",").map { it.trim() }.toSet(),
        metadata = UsersDto.MetaData(
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            deleted = this.deleted
        )
    )

}

//TODO MAPPER DEL CREATE A USERS

fun UsersCreateDto.toModel():Users{
    return Users(
        email=this.email,
        name=this.name,
        password=this.password,
        telephone = this.password.toInt(),
        rol= Users.TypeRol.valueOf(rol.uppercase()),
    )



}










