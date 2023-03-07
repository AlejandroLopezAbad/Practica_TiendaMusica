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
        println(property.getKey("mongoDb.uri"))
        mongoClient = KMongo.createClient(property.getKey("mongoDb.uri"))
            .coroutine
        println("Conectado")
        mongoDatabase = mongoClient.getDatabase(property.getKey("mongoDb.databaseName"))
    }
}