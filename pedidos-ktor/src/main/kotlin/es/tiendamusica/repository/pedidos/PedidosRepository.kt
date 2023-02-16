package es.tiendamusica.repository.pedidos

import es.tiendamusica.dtos.PedidoDto
import es.tiendamusica.models.Pedido
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.koin.core.annotation.Single
import org.litote.kmongo.Id
import service.mongo.MongoDbManager

@Single
class PedidosRepository : IPedidosRepository {
    override suspend fun findAll(): Flow<Pedido> {
        return MongoDbManager.mongoDatabase.getCollection<Pedido>()
            .find().publisher.asFlow()
    }

    override suspend fun findById(id: Id<Pedido>): Pedido? {
        return MongoDbManager.mongoDatabase.getCollection<Pedido>().findOneById(id)
    }


    override suspend fun save(entity: Pedido): Pedido {
        return MongoDbManager.mongoDatabase.getCollection<Pedido>().save(entity).let { entity }
    }


    override suspend fun delete(entity: Pedido): Boolean {
        return MongoDbManager.mongoDatabase.getCollection<Pedido>().deleteOneById(entity.id).let { true }
    }

    override suspend fun findByUser(id: String): Flow<Pedido> {
        return MongoDbManager.mongoDatabase.getCollection<Pedido>().find("{userId: '$id'}").publisher.asFlow()
    }
}