package com.example.microserviciousuarios.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Excepción específica de almacenamiento.
 */
sealed class StorageException : RuntimeException {
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}

/**
 * Excepción específica bad request, respuesta de código de estado del Protocolo
 * de Transferencia de Hipertexto (HTTP) 400.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
class StorageBadRequestException : StorageException {
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}

/**
 * Excepción específica, no se encuentra el archivo.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class StorageFileNotFoundException : StorageException {
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}