package es.tiendamusica.repository.pedidos

import es.tiendamusica.models.Pedido
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import service.mongo.MongoDbManager
import java.util.*

class PedidosRepository : IPedidosRepository {
    override suspend fun findAll(): Flow<Pedido> {
        return MongoDbManager.mongoDatabase.getCollection<Pedido>()
            .find().publisher.asFlow()
    }

    override suspend fun findById(id: UUID): Pedido? {
        return MongoDbManager.mongoDatabase.getCollection<Pedido>().findOneById(id)
    }

    override suspend fun save(entity: Pedido): Pedido {
        return MongoDbManager.mongoDatabase.getCollection<Pedido>().save(entity).let { entity }
    }

    override suspend fun delete(entity: Pedido): Boolean {
        return MongoDbManager.mongoDatabase.getCollection<Pedido>().deleteOneById(entity.id).let { true }
    }
}