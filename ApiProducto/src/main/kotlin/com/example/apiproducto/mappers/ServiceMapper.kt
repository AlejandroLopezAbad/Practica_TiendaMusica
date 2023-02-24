package com.example.apiproducto.mappers

import com.example.apiproducto.dto.ServiceCreateDto
import com.example.apiproducto.dto.ServiceDto
import com.example.apiproducto.models.Service
import com.example.apiproducto.models.ServiceCategory

fun Service.toServiceDto(): ServiceDto {
    return ServiceDto(
        category = this.category,
        description = this.description,
        price = this.price,
        url = this.url
    )
}

fun ServiceCreateDto.toService(): Service {
    return Service(
        price = this.price,
        category = ServiceCategory.valueOf(this.category.uppercase()),
        description = this.description,
        url = this.url,
        available = true
    )
}