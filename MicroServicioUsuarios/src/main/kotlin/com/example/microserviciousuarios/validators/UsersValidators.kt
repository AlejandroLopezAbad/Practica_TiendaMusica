package com.example.microserviciousuarios.validators

import com.example.microserviciousuarios.dto.UsersCreateDto

fun UsersCreateDto.validate(): UsersCreateDto {
    if (this.name.isBlank()) {
        throw Exception("El nombre no puede estar vacío") //TODO CAMBIAR EXCEPCIONES
    } else if (this.email.isBlank() || !this.email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)\$")))
        throw Exception("El email no puede estar vacío o no tiene el formato correcto")
    else if (this.password.isBlank() || this.password.length < 5)
        throw Exception("El password no puede estar vacío o ser menor de 5 caracteres")
    else if (this.rol.isBlank())
        throw Exception("No puedeno tener rol")
    return this
}
//TODO ver si queremos obligar que compruebe que tenga un rol