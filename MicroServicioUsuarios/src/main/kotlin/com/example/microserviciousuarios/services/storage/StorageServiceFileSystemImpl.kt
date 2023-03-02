package com.example.microserviciousuarios.services.storage


import com.example.microserviciousuarios.controller.StorageController
import com.example.microserviciousuarios.exceptions.StorageBadRequestException
import com.example.microserviciousuarios.exceptions.StorageFileNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class StorageServiceFileSystemImpl(
    @Value("\${upload.location}")
    private var path: String
): StorageService {

    private val location: Path

    init {
        location = Paths.get(path)
        this.init()
    }

    override fun init() {
        try {
            if(!Files.exists(location)){
                Files.createDirectory(location)
            }
        }catch (e: IOException) {
            throw StorageBadRequestException("Problemas para inilicializar el storage")
        }
    }

    override fun save(file: MultipartFile, username: String): String {
        val filename = StringUtils.cleanPath(file.originalFilename.toString())
        val extension = StringUtils.getFilenameExtension(filename).toString()
        val guardar ="$username.$extension"
        try {
            if (file.isEmpty) {
                throw StorageBadRequestException("Error fichero vacío $filename")

            }
            if (filename.contains("..")) {
                throw StorageBadRequestException("No se puede almacenar un fichero fuera del path permitido $filename")
            }
            file.inputStream.use { inputStream ->
                Files.copy(
                    inputStream, location.resolve(guardar),
                    StandardCopyOption.REPLACE_EXISTING
                )
                return guardar
            }
        } catch (e: IOException) {
            throw StorageBadRequestException("Fallo al almacenar fichero $filename -> $e")
        }
    }

    override fun load(filename: String): Resource {
        return try {
            val fichero = location.resolve(filename)
            val resource = UrlResource(fichero.toUri())
            if (resource.exists() || resource.isReadable) {
                resource
            } else {
                throw StorageFileNotFoundException(
                    "No se puede leer fichero: $filename"
                )
            }
        } catch (e: MalformedURLException) {
            throw StorageFileNotFoundException("No se puede leer fichero: $filename --> $e")
        }
    }

    override fun getUrl(filename: String): String {
        return MvcUriComponentsBuilder
            .fromMethodName(StorageController::class.java, "loadFile", filename, null)
            .build().toUriString()
    }
}
