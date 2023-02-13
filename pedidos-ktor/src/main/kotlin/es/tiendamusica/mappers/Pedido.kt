package es.tiendamusica.mappers

import es.tiendamusica.models.Pedido
import java.util.UUID

fun Pedido.toDto(){

}

fun String.toUUID() : UUID{
    return try {
        UUID.fromString(this.trim())
    }catch (e : IllegalArgumentException){
        throw Exception("El UUID no es correcto")
    }
}