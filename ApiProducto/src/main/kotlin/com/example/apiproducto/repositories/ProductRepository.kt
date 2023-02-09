package com.example.apiproducto.repositories

import com.example.apiproducto.models.Product
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface ProductRepository: R2dbcRepository<Product, Long> {
    fun findProductByUuid(uuid: String): Mono<Product>
}