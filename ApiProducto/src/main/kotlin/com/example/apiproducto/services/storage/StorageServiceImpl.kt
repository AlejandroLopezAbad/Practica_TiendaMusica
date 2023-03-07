package com.example.apiproducto.services.storage

import com.example.apiproducto.exceptions.StorageBadRequestException
import com.example.apiproducto.exceptions.StorageFileNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

/**
 * Servicio de almacenamiento de productos y servicios.
 */
@Service
class StorageServiceImpl(
    @Value("\${upload.location}") path: String,
) : StorageService {
    private val productLocation: Path
    private val serviceLocation: Path
    private val rootLocation: Path

    init {
        rootLocation = Paths.get(path)
        productLocation = Paths.get(path + File.separator + "products")
        serviceLocation = Paths.get(path + File.separator + "services")
        this.init()
    }


    /**
     * Creación de las carpetas de almacenamiento si no existiesen.
     * @throws StorageBadRequestException si diese problemas con la creación de las carpetas.
     */
    override fun init() {
        try {
            if (!Files.exists(rootLocation))
                Files.createDirectory(rootLocation)
            if (!Files.exists(productLocation)) {
                Files.createDirectory(productLocation)
            }
            if (!Files.exists(serviceLocation)) {
                Files.createDirectory(serviceLocation)
            }
        } catch (e: IOException) {
            throw StorageBadRequestException("No se puede inicializar el sistema de almacenamiento")
        }
    }


    /**
     * Almacenar un fichero en producto.
     * @param file fichero a almacenar.
     * @param uuidProduct uuid del producto que va a tener este fichero.
     * @throws StorageBadRequestException Si hay fallos con el almacenamiento.
     * @return nombre del fichero y extensión almacenado.
     */
    override fun storeProduct(file: MultipartFile, uuidProduct: String): String {
        val filename = StringUtils.cleanPath(file.originalFilename.toString())
        val extension = StringUtils.getFilenameExtension(filename).toString()
        val storedFilename = "$uuidProduct.$extension"
        try {
            if (file.isEmpty) {
                throw StorageBadRequestException("Fallo al almacenar un fichero de producto vacío $filename")
            }
            if (filename.contains("..")) {
                throw StorageBadRequestException("No se puede almacenar un fichero de producto fuera del path permitido $filename")
            }
            file.inputStream.use { inputStream ->
                Files.copy(
                    inputStream, productLocation.resolve(storedFilename),
                    StandardCopyOption.REPLACE_EXISTING
                )
                return storedFilename
            }
        } catch (e: IOException) {
            throw StorageBadRequestException("Fallo al almacenar fichero $filename en productos")
        }
    }


    /**
     * Almacenar un fichero en service.
     * @param file fichero a almacenar.
     * @param uuidService uuid del servicio que va a tener este fichero.
     * @throws StorageBadRequestException Si hay fallos con el almacenamiento.
     * @return nombre del fichero y extensión almacenado.
     */
    override fun storeService(file: MultipartFile, uuidService: String): String {
        val filename = StringUtils.cleanPath(file.originalFilename.toString())
        val extension = StringUtils.getFilenameExtension(filename).toString()
        val storedFilename = "$uuidService.$extension"
        try {
            if (file.isEmpty) {
                throw StorageBadRequestException("Fallo al almacenar un fichero de servicio vacío $filename")
            }
            if (filename.contains("..")) {
                throw StorageBadRequestException("No se puede almacenar un fichero de servicio fuera del path permitido $filename")
            }
            file.inputStream.use { inputStream ->
                Files.copy(
                    inputStream, serviceLocation.resolve(storedFilename),
                    StandardCopyOption.REPLACE_EXISTING
                )
                return storedFilename
            }
        } catch (e: IOException) {
            throw StorageBadRequestException("Fallo al almacenar fichero $filename en servicio")
        }
    }


    /**
     * Cargar los ficheros como resource.
     * @param filename nombre del fichero a buscar.
     * @param type si es de producto o servicio.
     * @return el fichero encontrado como resource.
     */
    override fun loadAsResource(filename: String, type: String): Resource {
        return try {
            if (type == "PRODUCT") {
                val file = productLocation.resolve(filename)
                val resource = UrlResource(file.toUri())
                if (resource.exists() || resource.isReadable) {
                    resource
                } else {
                    throw StorageFileNotFoundException("No se puede leer el fichero de productos: $filename")
                }
            } else {
                val file = serviceLocation.resolve(filename)
                val resource = UrlResource(file.toUri())
                if (resource.exists() || resource.isReadable) {
                    resource
                } else {
                    throw StorageFileNotFoundException("No se puede leer el fichero de servicios: $filename")
                }
            }
        } catch (e: MalformedURLException) {
            throw StorageFileNotFoundException("No se puede leer el fichero: $filename")
        }
    }


    /**
     * Eliminar un fichero de la carpeta product.
     * @param filename nombre del fichero a eliminar.
     * @throws StorageBadRequestException si da fallo para eliminar el fichero.
     */
    override fun deleteProduct(filename: String) {
        try {
            val file = productLocation.resolve(filename)
            Files.deleteIfExists(file)
        } catch (e: IOException) {
            throw StorageBadRequestException("Error al eliminar un fichero")
        }
    }


    /**
     * Eliminar un fichero de la carpeta service.
     * @param filename nombre del fichero a eliminar.
     * @throws StorageBadRequestException si da fallo para eliminar el fichero.
     */
    override fun deleteService(filename: String) {
        try {
            val file = serviceLocation.resolve(filename)
            Files.deleteIfExists(file)
        } catch (e: IOException) {
            throw StorageBadRequestException("Error al eliminar un fichero")
        }
    }

}