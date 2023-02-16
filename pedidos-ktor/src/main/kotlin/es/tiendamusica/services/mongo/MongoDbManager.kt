package service.mongo

import es.tiendamusica.utils.Property
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

object MongoDbManager {
    private var mongoClient: CoroutineClient
    var mongoDatabase: CoroutineDatabase

    init {
        val property= Property("mongo.properties")
        mongoClient = KMongo.createClient(property.getKey("mongoDb.uri"))
            .coroutine
        mongoDatabase = mongoClient.getDatabase(property.getKey("mongoDb.databaseName"))
    }
}