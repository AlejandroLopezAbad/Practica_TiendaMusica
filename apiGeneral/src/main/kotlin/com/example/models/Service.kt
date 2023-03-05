package com.example.models

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Service(
    var id: Int? = null,
    var uuid: String = UUID.randomUUID().toString(),
    var price: Double,
    var available: Boolean,
    var description: String,
    var url: String,
    var category: ServiceCategory,
)
enum class ServiceCategory {
    GUITAR_REPAIR, AMPLIFIER_REPAIR, CHANGE_OF_STRINGS
}

@Serializable
data class ServiceDto(
    var category: ServiceCategory,
    var description: String,
    var price: Double,
    var url: String,
)

@Serializable
data class ServiceCreateDto(
    var category: String,
    var description: String,
    var price: Double,
    var url: String,
)
@Serializable
data class ServiceUpdateDto(
    var description: String,
    var price: Double,
    var url: String,
    var available: Boolean,
)

