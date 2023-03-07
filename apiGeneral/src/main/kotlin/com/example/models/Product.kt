package com.example.models

import kotlinx.serialization.Serializable

@Serializable
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

@Serializable
data class ProductResponseDto(
    var uuid: String,
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

@Serializable
data class ProductUserResponseDto(
    var uuid: String,
    var name: String,
    var price: Double,
    var description: String,
    var url: String,
    var category: String,
    var stock: Int,
    var brand: String,
    var model: String
)