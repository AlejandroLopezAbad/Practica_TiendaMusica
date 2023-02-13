package es.tiendamusica.models

import java.util.*

data class Pedido(
    val id : Int,
    val uuid : UUID = UUID.randomUUID(),
    var price : Double
)
