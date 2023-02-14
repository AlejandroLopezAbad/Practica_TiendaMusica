package es.tiendamusica.controllers

import es.tiendamusica.dtos.PedidoDto
import es.tiendamusica.models.Pedido
import es.tiendamusica.repository.pedidos.PedidosRepository
import kotlinx.coroutines.flow.Flow
import mu.KotlinLogging

private val logger = KotlinLogging.logger { }
class PedidosController(private val repository : PedidosRepository) {

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

     suspend fun updarePedido(pedido: Pedido): Pedido{
        logger.debug("Updating Pedido")
         return repository.save(pedido)
    }

    suspend fun getAllOrders() : Flow<Pedido>{
        logger.debug("Getting all orders")
        return repository.findAll()
    }

    suspend fun deleteOrder(order: Pedido):Boolean{
        logger.debug("Deleting Order ${order.id}")
        return repository.delete(order)
    }

    suspend fun getOrderByUser(uuid: String): Flow<PedidoDto> {
        logger.debug("Obtaining users by uuid: $uuid")
        return repository.findPedidoByUser(uuid)
    }


}