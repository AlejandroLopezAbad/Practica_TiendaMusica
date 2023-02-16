package es.tiendamusica.dtos

import kotlinx.serialization.Serializable
import serializer.UUIDSerializer
import java.util.*

@Serializable
data class UserDto(
    @Serializable(UUIDSerializer::class)
    val uuid: UUID
)
