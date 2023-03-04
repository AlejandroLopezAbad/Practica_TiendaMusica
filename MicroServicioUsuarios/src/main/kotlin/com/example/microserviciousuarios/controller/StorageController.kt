package com.example.microserviciousuarios.controller

import com.example.microserviciousuarios.config.APIConfig
import com.example.microserviciousuarios.exceptions.StorageBadRequestException
import com.example.microserviciousuarios.exceptions.StorageException
import com.example.microserviciousuarios.models.Users
import com.example.microserviciousuarios.services.storage.StorageService
import jakarta.servlet.http.HttpServletRequest
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.time.LocalDateTime

/**
 * Controlador de la subida de archivos.
 */
@RestController
@RequestMapping(APIConfig.API_PATH + "/storage")
class StorageController
@Autowired constructor(
    private val service: StorageService
){
    /**
     * Save storage
     *
     * @param user
     * @param file
     * @return
     */
    @PostMapping(
        value = [""],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    suspend fun saveStorage(
        @AuthenticationPrincipal user: Users,
        @RequestPart("file") file: MultipartFile
    ): ResponseEntity<Map<String, String>> = runBlocking{
        return@runBlocking try {
            if (!file.isEmpty) {
                val fileStored = service.save(file, user.username)
                val urlStored = service.getUrl(fileStored)
                val response = mapOf("url" to urlStored ,"nombre" to fileStored, "creado" to LocalDateTime.now().toString())
                ResponseEntity(response, HttpStatus.CREATED)
            } else {
                throw StorageBadRequestException("No se puede subir un fichero vacío")
            }
        } catch (e: StorageException) {
            throw StorageBadRequestException(e.message.toString())
        }
    }

    /**
     * Load file
     *
     * @param filename
     * @param request
     * @return
     */
    @GetMapping(value = ["{filename:.+}"])
    @ResponseBody
    fun loadFile(
        @PathVariable filename: String?,
        request: HttpServletRequest
    ): ResponseEntity<Resource> = runBlocking {
        val file: Resource = service.load(filename.toString())
        var contentType: String? = null
        contentType = try {
            request.servletContext.getMimeType(file.file.absolutePath)
        } catch (e: IOException) {
            throw StorageBadRequestException("No se puede determinar el tipo del fichero --> $e")
        }
        if (contentType == null) {
            contentType = "application/octet-stream"
        }
        return@runBlocking ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .body<Resource?>(file)
    }
}