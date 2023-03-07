package com.example.apiproducto.repositories

import com.example.apiproducto.models.Service
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ServiceRepository : CoroutineCrudRepository<Service, Int> {
    fun findServiceByUuid(uuid: String): Flow<Service>
    fun findServiceByUrl(url: String): Flow<Service>
}
