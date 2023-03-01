package com.example.apiproducto.services

import com.example.apiproducto.dto.ServiceUpdateDto
import com.example.apiproducto.exceptions.ServiceNotFoundException
import com.example.apiproducto.repositories.ServiceRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import com.example.apiproducto.models.Service as Services

@Service
class ServicesService
@Autowired constructor(
    private val repository: ServiceRepository,
) {
    suspend fun findAllServices(): List<Services> {
        return repository.findAll().toList()
    }

    suspend fun findServiceById(id: Int): Services {
        return repository.findById(id)
            ?: throw ServiceNotFoundException("No se ha encontrado un servicio con el id: $id")
    }

    suspend fun findServiceByUuid(uuid: String): Services {
        return repository.findServiceByUuid(uuid).firstOrNull()
            ?: throw ServiceNotFoundException("No se ha encontrado un servicio con el uuid: $uuid")
    }

    suspend fun saveService(service: Services): Services {
        return repository.save(service)
    }

    suspend fun updateService(find: Services, service: ServiceUpdateDto): Services {
        return repository.save(
            Services(
                find.id,
                find.uuid,
                service.price,
                service.available,
                service.description,
                service.url,
                find.category
            )
        )
    }

    suspend fun deleteService(id: Int): Boolean {
        val exist = repository.findById(id)
        exist?.let {
            return repository.deleteById(id).let { true }
        } ?: throw ServiceNotFoundException("No existe el servicio con id: $id")
    }

    suspend fun notAvailableService(id: Int): Boolean {
        val exist = repository.findById(id)
        exist?.let {
            repository.save(
                Services(
                    it.id,
                    it.uuid,
                    it.price,
                    false,
                    it.description,
                    it.url,
                    it.category
                )
            )
            return true
        } ?: throw ServiceNotFoundException("No existe el servicio con id: $id")
    }
}