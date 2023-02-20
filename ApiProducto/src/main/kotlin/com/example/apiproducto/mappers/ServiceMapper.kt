package com.example.apiproducto.mappers

import com.example.apiproducto.dto.ServiceCreateDto
import com.example.apiproducto.dto.ServiceDto
import com.example.apiproducto.models.Service
import com.example.apiproducto.models.ServiceCategory

fun Service.toServiceDto(): ServiceDto {
    return ServiceDto(
        price = this.price,
        description = this.description,
        url = this.url,
        category = this.category.name
    )
}

fun ServiceDto.toService(): Service {
    return Service(
        price = this.price,
        available = true,
        description = this.description,
        url = this.url,
        category = ServiceCategory.valueOf(this.category.uppercase())
    )
}

fun ServiceCreateDto.toService(): Service {
    return Service(
        price = this.price,
        available = this.available,
        description = this.description,
        url = this.url,
        category = ServiceCategory.valueOf(this.category.uppercase())
    )
}