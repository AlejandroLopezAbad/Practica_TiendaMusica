package es.tiendamusica.repository.pedidos

import es.tiendamusica.models.Pedido
import es.tiendamusica.repository.ICRUD
import kotlinx.coroutines.flow.Flow
import org.litote.kmongo.Id

interface IPedidosRepository : ICRUD<Pedido, Id<Pedido>> {
    suspend fun findByUser(id: String): Flow<Pedido>
}