package com.example.apiproducto.controller

import com.example.apiproducto.models.Product
import com.example.apiproducto.models.ProductCategory
import com.example.apiproducto.repositories.ProductRepository
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/product")
class ProductController
@Autowired constructor(
    private var repository: ProductRepository
){
    @GetMapping("/guitar")
    suspend fun getGuitarProducts(): ResponseEntity<List<Product>>{
        val guitars = repository.findProductsByCategory(ProductCategory.GUITAR.name).toList()
        return ResponseEntity.ok(guitars)
    }
}