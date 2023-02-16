package es.tiendamusica.repository

import kotlinx.coroutines.flow.Flow

interface ICRUD<T, ID> {
    suspend fun findAll(): Flow<T>
    suspend fun findById(id: ID): T?
    suspend fun save(entity: T): T
    suspend fun delete(entity: T): Boolean
}