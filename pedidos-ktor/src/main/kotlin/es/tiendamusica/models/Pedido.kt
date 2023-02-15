package es.tiendamusica.models

import es.tiendamusica.dtos.PedidoCreateDto
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId
import serializer.LocalDateSerializer
import java.time.LocalDate
import java.util.*

@Serializable
data class Pedido(
    @BsonId @Contextual
    val id: String = newId<Pedido>().toString(), // no cambia
    val uuid: String = UUID.randomUUID().toString(), // no cambia
    var price: Double, // si cambia
    var userId: String, // no cambia
    var status: Status, // si cambia
    @Serializable(LocalDateSerializer::class)
    var createdAt: LocalDate, // no cambia
    @Serializable(LocalDateSerializer::class)
    var deliveredAt: LocalDate? // si cambia
) {
    enum class Status(val status: String) {
        RECEIVED("Received"),
        PROCESS("Process"),
        FINISHED("Finished");

        companion object {
            fun from(estado: String): Status {
                return when (estado) {
                    "Recieved" -> RECEIVED
                    "Process" -> PROCESS
                    "Finished" -> FINISHED
                    else -> throw Exception("Status don´t exist")
                }
            }
        }
    }
}

fun PedidoCreateDto.toModel() = Pedido(
    price = this.price,
    userId = this.userId,
    status = Pedido.Status.RECEIVED,
    createdAt = LocalDate.now(),
    deliveredAt = null
)
