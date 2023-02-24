package com.example.apiproducto.dto

/**
 * Dto para añadir y actualizar productos en la API
 */
data class ProductDto(
    var name: String,
    var price: Double,
    var available: Boolean,
    var description: String,
    var url: String,
    var category: String,
    var stock: Int,
    var brand: String,
    var model: String
)


/**
 * Dto de respuesta con el usuario es de tipo user
 */
data class ProductUserDto(
    var name: String,
    var price: Double,
    var description: String,
    var url: String,
    var category: String,
    var stock: Int,
    var brand: String,
    var model: String
)