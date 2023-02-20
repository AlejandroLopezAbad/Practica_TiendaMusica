package com.example.apiproducto.dto

data class ServiceCreateDto(
    var price: Double,
    var available: Boolean,
    var description: String,
    var url: String,
    var category: String,
)

// Dto para cuando el cliente busque el servicio no vea ni la id, uuid
// y si esta disponible o no
data class ServiceDto(
    var price: Double,
    var description: String,
    var url: String,
    var category: String,
)