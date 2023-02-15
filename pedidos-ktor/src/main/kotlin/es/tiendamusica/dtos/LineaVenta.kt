package es.tiendamusica.dtos

import kotlinx.serialization.Serializable

@Serializable
data class LineaVenta(
    val idItem: String,
    val precio: Double,
    val cantidad: Int
)
