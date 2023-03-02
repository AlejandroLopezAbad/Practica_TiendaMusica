package com.example.microserviciousuarios.services.storage


import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path
import java.util.stream.Stream


interface StorageService {
    fun init()
    fun save(file: MultipartFile, username:String): String
    fun load(filename:String): Resource
    fun getUrl(filename: String): String
}
