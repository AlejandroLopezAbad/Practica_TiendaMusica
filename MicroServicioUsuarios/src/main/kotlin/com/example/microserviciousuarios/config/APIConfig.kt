package com.example.microserviciousuarios.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

/**
 * A p i config
 *
 * @constructor Create empty A p i config
 */
@Configuration
class APIConfig {
    companion object{
        @Value("\${api.path}")
        const val API_PATH = "/api"


    }
}