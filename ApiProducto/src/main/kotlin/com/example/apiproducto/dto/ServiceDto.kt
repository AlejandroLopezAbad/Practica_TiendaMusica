package com.example.apiproducto.dto

import com.example.apiproducto.models.ServiceCategory

/**
 * Modelo del servicio que ve el cliente.
 */
data class ServiceDto(
    var category: ServiceCategory,
    var description: String,
    var price: Double,
    var url: String,
)

/**
 * Modelo de servicio para el post
 */
data class ServiceCreateDto(
    var category: String,
    var description: String,
    var price: Double,
    var url: String,
)

/**
 * Modelo de servicio para el put
 */
data class ServiceUpdateDto(
    var description: String,
    var price: Double,
    var url: String,
    var available: Boolean,
)