package com.example.apiproducto.validators

import com.example.apiproducto.dto.ServiceCreateDto
import com.example.apiproducto.dto.ServiceUpdateDto
import com.example.apiproducto.exceptions.ServiceBadRequestException
import com.example.apiproducto.models.ServiceCategory

fun ServiceCreateDto.validate(): ServiceCreateDto {
    if (!ServiceCategory.values().map { it.toString() }.contains(this.category.uppercase()))
        throw ServiceBadRequestException("La categoría es incorrecta")
    if (this.description.isBlank())
        throw ServiceBadRequestException("La descripción no puede estar vacía")
    if (this.price <= 0)
        throw ServiceBadRequestException("El precio no puede ser igual o inferior a 0.")
    if (this.url.isBlank())
        throw ServiceBadRequestException("La url no puede estar vacía.")
    return this
}

fun ServiceUpdateDto.validate(): ServiceUpdateDto {
    if (this.description.isBlank())
        throw ServiceBadRequestException("La descripción no puede estar vacía")
    if (this.price <= 0)
        throw ServiceBadRequestException("El precio no puede ser igual o inferior a 0.")
    if (this.url.isBlank())
        throw ServiceBadRequestException("La url no puede estar vacía.")
    return this
}