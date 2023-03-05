package es.tiendamusica.repository.pedidos

import es.tiendamusica.models.Order
import es.tiendamusica.repository.ICRUD
import kotlinx.coroutines.flow.Flow
import org.litote.kmongo.Id

interface IOrderRepository : ICRUD<Order, Id<Order>> {
    suspend fun findByUser(id: String): Flow<Order>

}