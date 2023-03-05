package com.example.microserviciousuarios.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

/**
 * Clase de configuración con la información básica de la API.
 */
@Configuration
class APIConfig {
    companion object{
        @Value("\${api.path}")
        const val API_PATH = "/api"


    }
}