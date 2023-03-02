package es.tiendamusica.dtos

import es.tiendamusica.models.Order
import kotlinx.serialization.Serializable
import serializer.LocalDateSerializer
import serializer.UUIDSerializer
import java.time.LocalDate
import java.util.*

@Serializable
data class OrderDto(
    val id: String,
    @Serializable(UUIDSerializer::class)
    val uuid: UUID,
    val price: Double,
    val user: UserDto,
    val status: Order.Status,
    @Serializable(LocalDateSerializer::class)
    val createdAt: LocalDate,
    @Serializable(LocalDateSerializer::class)
    val deliveredAt: LocalDate?
)

@Serializable
data class OrderCreateDto(
    val price: Double,
    val products: List<SellLine>,
    val userId: String
)

@Serializable
data class OrderUpdateDto(
    val price: Double? = null,
    val status: Order.Status? = null
)

@Serializable
data class OrderPageDto(
    val page: Int,
    val perPage: Int,
    val orders: List<OrderDto>
)

