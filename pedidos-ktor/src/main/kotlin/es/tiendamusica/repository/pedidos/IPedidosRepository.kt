package es.tiendamusica.repository.pedidos

import es.tiendamusica.models.Pedido
import es.tiendamusica.repository.ICRUD
import java.util.*

interface IPedidosRepository : ICRUD<Pedido, UUID> {
    //suspend fun findByUserId(id : UUID) : User?
}