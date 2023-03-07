package com.example.microserviciousuarios.validators

import com.example.microserviciousuarios.dto.UsersCreateDto
import com.example.microserviciousuarios.dto.UsersUpdateDto
import com.example.microserviciousuarios.exceptions.UsersBadRequestException

/**
 * Método que valida los datos para la creación de los usuarios.
 */
fun UsersCreateDto.validate(): UsersCreateDto {
    if (this.name.isBlank()) {
        throw UsersBadRequestException("El nombre no puede estar vacío")
    } else if (this.email.isBlank() || !this.email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)\$")))
        throw UsersBadRequestException("El email no puede estar vacío o no tiene el formato correcto")
    else if (this.password.isBlank() || this.password.length < 5)
        throw UsersBadRequestException("El password no puede estar vacío o ser menor de 5 caracteres")
    return this
}

/**
 * Método que valida los datos para la actualización de usuarios.
 */
fun UsersUpdateDto.validate(): UsersUpdateDto {
     if (this.email.isBlank() || !this.email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)\$")))
        throw UsersBadRequestException("El email no puede estar vacío o no tiene el formato correcto")
    else if (this.name.isBlank())
        throw UsersBadRequestException("El name no puede estar vacío")
    else if (this.telephone.isBlank())
       throw UsersBadRequestException("El telephone no puede estar vacio o ser menor de 5 caracteres")
    return this
}