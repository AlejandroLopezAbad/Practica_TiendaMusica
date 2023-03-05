package es.tiendamusica.models

import es.tiendamusica.dtos.OrderCreateDto
import es.tiendamusica.dtos.SellLine
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId
import serializer.LocalDateSerializer
import java.time.LocalDate
import java.util.*

@Serializable
data class Order(
    @BsonId @Contextual
    val id: String = newId<Order>().toString(), // no cambia
    val uuid: String = UUID.randomUUID().toString(), // no cambia
    var price: Double, // si cambia
    var userId: String, // no cambia
    var status: Status, // si cambia
    @Serializable(LocalDateSerializer::class)
    var createdAt: LocalDate, // no cambia
    @Serializable(LocalDateSerializer::class)
    var deliveredAt: LocalDate?, // si cambia
    val productos: MutableList<SellLine>
) {
    enum class Status(val status: String) {
        RECEIVED("Received"),
        PROCESSING("Processing"),
        FINISHED("Finished");

        companion object {
            fun from(estado: String): Status {
                return when (estado) {
                    "Recieved" -> RECEIVED
                    "Processing" -> PROCESSING
                    "Finished" -> FINISHED
                    else -> throw Exception("Status doesn´t exist")
                }
            }
        }
    }
}

fun OrderCreateDto.toModel() = Order(
    price = this.products.sumOf { it.price * it.quantity },
    uuid = UUID.randomUUID().toString(),
    userId = this.userId,
    status = Order.Status.PROCESSING,
    createdAt = LocalDate.now(),
    deliveredAt = null,
    productos = this.products as MutableList<SellLine>
)
