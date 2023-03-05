package es.tiendamusica.controllers

import es.tiendamusica.dtos.OrderUpdateDto
import es.tiendamusica.models.Order
import es.tiendamusica.repository.pedidos.OrderRepository
import kotlinx.coroutines.flow.Flow
import mu.KotlinLogging
import org.koin.core.annotation.Single
import org.litote.kmongo.toId
import java.time.LocalDate

private val logger = KotlinLogging.logger { }

@Single
class OrderController(private val repository: OrderRepository) {

    suspend fun createOrder(order: Order): Order {
        logger.debug("Trying to find User...")
        try {
            // Aqui iría el metodo con el repositorio de usuario
            //aqui busca en el repositorio de usuario por el pedido.usuario -> id referenciado
            logger.debug("Creating Order:  ${order.uuid}")
            return repository.save(order)
        } catch (e: Exception) {
            throw Exception(e.message!!)
        }
    }

    suspend fun updateOrder(order: Order): Order {
        logger.debug("Updating Pedido")
        if (order.status == Order.Status.FINISHED)
            order.deliveredAt = LocalDate.now()
        return repository.save(order)
    }

    suspend fun patchOrder(order: Order, newData: OrderUpdateDto): Order {
        logger.debug { "Patching pedido" }
        if (newData.price != null)
            order.price = newData.price
        if (newData.status != null)
            order.status = newData.status

        return repository.save(order)
    }

    suspend fun getAllOrders(): Flow<Order> {
        logger.debug("Getting all orders")
        return repository.findAll()
    }

    suspend fun getById(id: String): Order? {
        logger.debug { "Getting order by id" }
        return repository.findById(id.toId())
    }

    suspend fun getByUserId(id: String): Flow<Order> {
        logger.debug { "Getting all orders from user : $id" }
        return repository.findByUser(id)
    }

    suspend fun deleteOrder(order: Order): Boolean {
        logger.debug("Deleting Order ${order.id}")
        return repository.delete(order)
    }

    suspend fun getOrderByUser(uuid: String): Flow<Order> {
        logger.debug("Obtaining users by uuid: $uuid")
        return repository.findByUser(uuid)
    }

    suspend fun getAllOrders(page: Int, perPage: Int): Flow<Order> {
        return repository.findAll(page, perPage)

    }


}