package es.tiendamusica.mappers

import es.tiendamusica.dtos.OrderDto
import es.tiendamusica.dtos.UserDto
import es.tiendamusica.models.Order
import java.util.*

fun Order.toDto() = OrderDto(
    id = this.id.toString(),
    uuid = UUID.fromString(this.uuid),
    price = this.price,
    user = UserDto(UUID.randomUUID()),
    status = this.status,
    createdAt = this.createdAt,
    deliveredAt = this.deliveredAt


)

fun String.toUUID(): UUID {
    return try {
        UUID.fromString(this.trim())
    } catch (e: IllegalArgumentException) {
        throw Exception("El UUID no es correcto")
    }
}