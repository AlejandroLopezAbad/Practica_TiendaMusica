package com.example.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import serializer.LocalDateSerializer
import serializer.UUIDSerializer
import java.time.LocalDate
import java.util.*

@Serializable
data class Order(
    val id: String ,
    val uuid: String,
    var price: Double,
    var userId: String,
    var status: Status,
    @Serializable(LocalDateSerializer::class)
    var createdAt: LocalDate?, // no cambia
    @Serializable(LocalDateSerializer::class)
    var deliveredAt: LocalDate?, // si cambia
    val productos: MutableList<SellLine>
)


@Serializable
data class SellLine(
    val idItem: String,
    val price: Double,
    val quantity: Int
)

enum class Status(val status: String) {
    RECEIVED("Received"),
    PROCESSING("Processing"),
    FINISHED("Finished");
}

@Serializable
data class OrderDto(
    val id: String,
    @Serializable(UUIDSerializer::class)
    val uuid: UUID,
    val price: Double,
    val user: UserDto,
    val status: Status,
    @Serializable(LocalDateSerializer::class)
    val createdAt: LocalDate,
    @Serializable(LocalDateSerializer::class)
    val deliveredAt: LocalDate?
)

@Serializable
data class OrderCreateDto(
    val price: Double,
    val products: List<SellLine> = mutableListOf(),
    val userId: String
)

@Serializable
data class OrderUpdateDto(
    val price: Double? = null,
    val status: Status? = null
)


