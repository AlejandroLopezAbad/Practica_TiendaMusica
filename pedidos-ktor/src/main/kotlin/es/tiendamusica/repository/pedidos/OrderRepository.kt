package es.tiendamusica.repository.pedidos

import es.tiendamusica.models.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.koin.core.annotation.Single
import org.litote.kmongo.Id
import service.mongo.MongoDbManager

@Single
class OrderRepository : IOrderRepository {
    override suspend fun findAll(): Flow<Order> {
        return MongoDbManager.mongoDatabase.getCollection<Order>()
            .find().publisher.asFlow()
    }

    suspend fun findAll(page: Int, perPage: Int): Flow<Order> {
        return MongoDbManager.mongoDatabase.getCollection<Order>()
            .find()
            .skip(page * perPage)
            .limit(perPage)
            .publisher.asFlow()
    }

    override suspend fun findById(id: Id<Order>): Order? {
        return MongoDbManager.mongoDatabase.getCollection<Order>().findOneById(id)
    }


    override suspend fun save(entity: Order): Order {
        return MongoDbManager.mongoDatabase.getCollection<Order>().save(entity).let { entity }
    }


    override suspend fun delete(entity: Order): Boolean {
        return MongoDbManager.mongoDatabase.getCollection<Order>().deleteOneById(entity.id).let { true }
    }

    override suspend fun findByUser(id: String): Flow<Order> {
        return MongoDbManager.mongoDatabase.getCollection<Order>().find("{userId: '$id'}").publisher.asFlow()
    }
}