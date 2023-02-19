package com.example.microserviciousuarios.mappers

import com.example.microserviciousuarios.dto.UserDto
import com.example.microserviciousuarios.models.RoleCategory
import com.example.microserviciousuarios.models.User


fun UserDto.toUser(): User {
    return User(
        email = email,
        name = name,
        password = password,
        telephone = telephone,
        role = RoleCategory.valueOf(role),
        available = available,
        url = url
    )
}

