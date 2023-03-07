package com.example.apiproducto.services

import com.example.apiproducto.dto.ServiceUpdateDto
import com.example.apiproducto.exceptions.ProductNotFoundException
import com.example.apiproducto.exceptions.ServiceNotFoundException
import com.example.apiproducto.models.Product
import com.example.apiproducto.repositories.ServiceRepository
import com.example.apiproducto.services.storage.StorageService
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import com.example.apiproducto.models.Service as Services

/**
 * Servicio de servicios.
 * @property repository repositorio de servicios
 */
@Service
class ServicesService
@Autowired constructor(
    private val repository: ServiceRepository,
    private val storage: StorageService
) {

    /**
     * Buscar todos los servicios.
     * @return lista de todos los servicios.
     */
    suspend fun findAllServices(): List<Services> {
        return repository.findAll().toList()
    }


    /**
     * Buscar un servicio por su id.
     * @param id id del servicio a buscar.
     * @throws ServiceNotFoundException si no se encuentra el servicio.
     * @return el servicio encontrado.
     */
    suspend fun findServiceById(id: Int): Services {
        return repository.findById(id)
            ?: throw ServiceNotFoundException("No se ha encontrado un servicio con el id: $id")
    }


    /**
     * Buscar un servicio por su uuid.
     * @param uuid uuid del servicio a buscar.
     * @throws ServiceNotFoundException si no se encuentra el servicio.
     * @return el servicio encontrado.
     */
    suspend fun findServiceByUuid(uuid: String): Services {
        return repository.findServiceByUuid(uuid).firstOrNull()
            ?: throw ServiceNotFoundException("No se ha encontrado un servicio con el uuid: $uuid")
    }


    /**
     * Guardar un servicio.
     * @param service servicio a guardar en el repositorio.
     * @return el servicio que se ha guardado.
     */
    suspend fun saveService(service: Services): Services {
        return repository.save(service)
    }


    /**
     * Actualizar un servicio.
     * @param find el servicio que tenemos almacenado.
     * @param service servicio con los nuevos datos.
     * @return el servicio ya actualizado.
     */
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

    /**
     * Eliminar un servicio.
     * @param uuid uuid del servicio a eliminar.
     * @throws ServiceNotFoundException si no se encuentra el servicio a eliminar.
     * @return boolean si se ha eliminado correctamente.
     */
    suspend fun deleteService(uuid: String): Boolean {
        val exist = repository.findServiceByUuid(uuid).firstOrNull()
        exist?.let {
            if(it.url != "placeholder.jpg"){
                storage.deleteService(it.url)
            }
            return repository.deleteById(it.id!!).let { true }
        } ?: throw ServiceNotFoundException("No existe el servicio con id: $uuid")
    }

    /**
     * Actualizar la disponibilidad de un servicio.
     * @param uuid uuid del servicio a actualizar la disponibilidad.
     * @throws ServiceNotFoundException si no se encuentra el servicio a actualizar.
     * @return boolean dependiendo de si se ha actualizado correctamente.
     */
    suspend fun notAvailableService(uuid: String): Boolean {
        val exist = repository.findServiceByUuid(uuid).firstOrNull()
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
        } ?: throw ServiceNotFoundException("No existe el servicio con id: $uuid")
    }

    /**
     * Cambiar la url del servicio.
     * @param uuid uuid del servicio.
     * @param url url nueva del servicio.
     * @throws ProductNotFoundException si no se encuentra el servicio con ese uuid.
     * @return boolean si ha sido cambiado correctamente
     */
    suspend fun changeUrlService(uuid:String, url: String): Boolean{
        val exist = repository.findServiceByUuid(uuid).firstOrNull()
        exist?.let {
            val service = Services(
                id = exist.id,
                uuid = exist.uuid,
                price = exist.price,
                available = exist.available ,
                description = exist.description,
                url = url,
                category = exist.category
            )
            repository.save(service)
            return true
        } ?: throw ServiceNotFoundException("No existe el servicio con uuid: $uuid")
    }


    /**
     * Cambiar la url del producto.
     * @param filename filename almacenado en el servicio de la BDD.
     * @param url url nueva del servicio.
     * @throws ProductNotFoundException si no se encuentra el servicio con ese uuid.
     * @return boolean si ha sido cambiado correctamente
     */
    suspend fun deleteUrlService(filename:String, url: String): Boolean{
        val exist = repository.findServiceByUrl(filename).firstOrNull()
        exist?.let {
            val service = Services(
                id = exist.id,
                uuid = exist.uuid,
                price = exist.price,
                available = exist.available ,
                description = exist.description,
                url = url,
                category = exist.category
            )
            repository.save(service)
            return true
        } ?: throw ServiceNotFoundException("No existe el servicio con url: $url")
    }
}