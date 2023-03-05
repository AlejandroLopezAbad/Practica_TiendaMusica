package com.example.microserviciousuarios.services.storage

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile


/**
 * Storage service
 *
 * @constructor Create empty Storage service
 */
interface StorageService {

    /**
     * Init
     *
     */
    fun init()

    /**
     * Save
     *
     * @param file
     * @param username
     * @return
     */
    fun save(file: MultipartFile, username:String): String

    /**
     * Load
     *
     * @param filename
     * @return
     */
    fun load(filename:String): Resource

    /**
     * Get url
     *
     * @param filename
     * @return
     */
    fun getUrl(filename: String): String
}
