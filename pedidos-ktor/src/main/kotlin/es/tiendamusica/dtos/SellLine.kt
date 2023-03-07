package es.tiendamusica.dtos

import kotlinx.serialization.Serializable

@Serializable
data class SellLine(
    val idItem: String,
    val price: Double,
    val quantity: Int
)
