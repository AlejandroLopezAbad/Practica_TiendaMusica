package com.example.apiproducto.services.storage

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile

interface StorageService {
    fun init()
    fun storeProduct(file: MultipartFile, uuidProduct: String): String
    fun storeService(file: MultipartFile, uuidService:String): String
    fun loadAsResource(filename: String, type:String): Resource
    fun deleteProduct(filename: String)
    fun deleteService(filename: String)
}