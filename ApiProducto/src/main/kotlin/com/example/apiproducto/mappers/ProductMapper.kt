package com.example.apiproducto.mappers

import com.example.apiproducto.dto.ProductDto
import com.example.apiproducto.dto.ProductResponseDto
import com.example.apiproducto.dto.ProductUserResponseDto
import com.example.apiproducto.models.Product
import com.example.apiproducto.models.ProductCategory

fun ProductDto.toProduct():Product{
    return Product(
        name = name,
        price = price,
        available = available,
        description = description,
        url = url,
        category = ProductCategory.valueOf(category.uppercase()),
        stock = stock,
        brand = brand,
        model = model
    )
}


fun Product.toProductResponseDto(): ProductResponseDto {
    return ProductResponseDto(
        uuid = uuid,
        name = name,
        price = price,
        available = available,
        description = description,
        url = url,
        category = category.name,
        stock = stock,
        brand = brand,
        model = model
    )
}

fun Product.toProductUserResponseDto(): ProductUserResponseDto{
    return ProductUserResponseDto(
        uuid = uuid,
        name = name,
        price = price,
        description = description,
        url = url,
        category = category.name,
        stock = stock,
        brand = brand,
        model = model
    )
}