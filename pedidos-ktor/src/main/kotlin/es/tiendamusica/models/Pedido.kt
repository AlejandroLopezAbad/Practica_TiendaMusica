package es.tiendamusica.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import serializer.LocalDateSerializer
import java.time.LocalDate
import java.util.*
@Serializable
data class Pedido(
    @BsonId @Contextual
    val id : Id<Pedido> = newId(),
    val uuid : String = UUID.randomUUID().toString(),
    var price : Double,
    var usuario :User,
    var status: Status,
    @Serializable(LocalDateSerializer::class)
    var createdAt: LocalDate,
    @Serializable(LocalDateSerializer::class)
    var deliveredAt: LocalDate
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
