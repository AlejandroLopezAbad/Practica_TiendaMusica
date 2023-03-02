package com.example.apiproducto.controller

import com.example.apiproducto.exceptions.ProductNotFoundException
import com.example.apiproducto.exceptions.ServiceNotFoundException
import com.example.apiproducto.exceptions.StorageBadRequestException
import com.example.apiproducto.services.ProductService
import com.example.apiproducto.services.ServicesService
import com.example.apiproducto.services.storage.StorageService
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/storage")
class StorageController
@Autowired constructor(
    private val storageService: StorageService,
    private val productService: ProductService,
    private val servicesService: ServicesService
){

    @PostMapping(
        value = ["/product/{uuid}"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun storeProduct(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String,
        @RequestPart("file") file: MultipartFile,
        @PathVariable uuid: String
    ): ResponseEntity<Map<String, String>> = runBlocking {
        return@runBlocking try{
            productService.findProductByUuid(uuid)
            if(!file.isEmpty){
                val saved = storageService.storeProduct(file, uuid)
                val response = mapOf("name" to saved, "created_at" to LocalDateTime.now().toString())
                ResponseEntity.status(HttpStatus.CREATED).body(response)
            }else{
             throw ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede almacenar un fichero vacío")
            }
        }catch (e: StorageBadRequestException){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }catch (e: ProductNotFoundException){
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        }
    }


    @PostMapping(
        value = ["/service/{uuid}"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun storeService(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String,
        @RequestPart("file") file: MultipartFile,
        @PathVariable uuid: String
    ): ResponseEntity<Map<String, String>> = runBlocking {
        return@runBlocking try{
            servicesService.findServiceByUuid(uuid)
            if(!file.isEmpty){
                val saved = storageService.storeService(file, uuid)
                val response = mapOf("name" to saved, "created_at" to LocalDateTime.now().toString())
                ResponseEntity.status(HttpStatus.CREATED).body(response)
            }else{
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede almacenar un fichero vacío")
            }
        }catch (e: StorageBadRequestException){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }catch (e: ServiceNotFoundException){
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        }
    }


}