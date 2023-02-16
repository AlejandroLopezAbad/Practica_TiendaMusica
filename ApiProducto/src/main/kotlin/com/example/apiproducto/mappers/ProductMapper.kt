package com.example.apiproducto.mappers

import com.example.apiproducto.dto.ProductDto
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