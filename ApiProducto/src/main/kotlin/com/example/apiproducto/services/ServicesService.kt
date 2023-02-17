package com.example.apiproducto.services

import com.example.apiproducto.repositories.ServiceRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import com.example.apiproducto.models.Service as Services

@Service
class ServicesService
@Autowired constructor(
    private val repository: ServiceRepository,
) {
    suspend fun findAllServices(): Flow<Services> {
        return repository.findAll()
    }

    suspend fun findById(id: Int): Services? {
        return repository.findById(id)
    }

    suspend fun saveService(service: Services): Services {
        return repository.save(service)
    }

    suspend fun updateService(find: Services, service: Services): Services {
        return repository.save(
            Services(
                find.id,
                find.uuid,
                service.price,
                service.available,
                service.description,
                service.url,
                service.category
            )
        )
    }

    suspend fun deleteService(id: Int): Boolean {
        return repository.deleteById(id).let { true }
    }
}