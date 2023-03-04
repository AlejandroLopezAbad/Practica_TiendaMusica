package com.example.models

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Product(
    var id: Int? = null,
    var uuid: String = UUID.randomUUID().toString(),
    var name: String,
    var price: Double,
    var available: Boolean,
    var description: String,
    var url: String,
    var category: ProductCategory,
    var stock: Int,
    var brand: String,
    var model: String
)

enum class ProductCategory {
    GUITAR, BASS_GUITAR, BOOSTER, ACCESSORY
}

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