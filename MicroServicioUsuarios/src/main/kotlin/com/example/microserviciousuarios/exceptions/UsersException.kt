package com.example.microserviciousuarios.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Clase que desarrolla y determina las excepciones de los usuarios.
 */
sealed class UsersException(message: String) : RuntimeException(message)

/**
 * Excepción específica, código 404 no encontrado.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class UsersNotFoundException(message: String) : RuntimeException(message)

/**
 * Excepción específica, código 400, error percibido del cliente.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
class UsersBadRequestException(message: String) : RuntimeException(message)