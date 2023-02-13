package es.tiendamusica.dtos

import es.tiendamusica.models.Pedido
import kotlinx.serialization.Serializable
import serializer.LocalDateSerializer
import java.time.LocalDate
import java.util.UUID

data class PedidoDto(
    val id: String,
    val uuid: UUID,
    val price: Double,
    val user: UserDto,
    val status: Pedido.Status,
    @Serializable(LocalDateSerializer::class)
    val createdAt: LocalDate,
    @Serializable(LocalDateSerializer::class)
    val deliveredAt: LocalDate
)
