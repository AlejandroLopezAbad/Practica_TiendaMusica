package es.tiendamusica.controllers

import es.tiendamusica.dtos.UpdatePedidoDto
import es.tiendamusica.models.Pedido
import es.tiendamusica.repository.pedidos.PedidosRepository
import kotlinx.coroutines.flow.Flow
import mu.KotlinLogging
import org.koin.core.annotation.Single
import org.litote.kmongo.toId
import java.time.LocalDate

private val logger = KotlinLogging.logger { }

@Single
class PedidosController(private val repository: PedidosRepository) {

    suspend fun createOrder(pedido: Pedido): Pedido {
        logger.debug("Trying to find User...")
        try {
            // Aqui iría el metodo con el repositorio de usuario
            //aqui busca en el repositorio de usuario por el pedido.usuario -> id referenciado
            logger.debug("Creating Order:  ${pedido.uuid}")
            return repository.save(pedido)
        } catch (e: Exception) {
            throw Exception(e.message!!)
        }
    }

    suspend fun updatePedido(pedido: Pedido): Pedido {
        logger.debug("Updating Pedido")
        if (pedido.status == Pedido.Status.FINISHED)
            pedido.deliveredAt = LocalDate.now()
        return repository.save(pedido)
    }

    suspend fun patchPedido(pedido: Pedido, newData: UpdatePedidoDto): Pedido {
        logger.debug { "Patching pedido" }
        if (newData.precio != null)
            pedido.price = newData.precio
        if (newData.status != null)
            pedido.status = newData.status

        return repository.save(pedido)
    }

    suspend fun getAllOrders(): Flow<Pedido> {
        logger.debug("Getting all orders")
        return repository.findAll()
    }

    suspend fun getById(id: String): Pedido? {
        logger.debug { "Getting order by id" }
        return repository.findById(id.toId())
    }

    suspend fun deleteOrder(order: Pedido): Boolean {
        logger.debug("Deleting Order ${order.id}")
        return repository.delete(order)
    }


}