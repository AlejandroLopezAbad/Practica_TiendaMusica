package com.example.apiproducto.controller

import com.example.apiproducto.dto.ProductDto
import com.example.apiproducto.mappers.toProduct
import com.example.apiproducto.models.Product
import com.example.apiproducto.models.ProductCategory
import com.example.apiproducto.repositories.ProductRepository
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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

    @GetMapping("/bass_guitar")
    suspend fun getBassGuitarProducts(): ResponseEntity<List<Product>>{
        val bass = repository.findProductsByCategory(ProductCategory.BASS_GUITAR.name).toList()
        return ResponseEntity.ok(bass)
    }

    @GetMapping("/booster")
    suspend fun getBoosterProducts(): ResponseEntity<List<Product>>{
        val booster = repository.findProductsByCategory(ProductCategory.BOOSTER.name).toList()
        return ResponseEntity.ok(booster)
    }

    @GetMapping("/accessory")
    suspend fun getAccessoryProducts(): ResponseEntity<List<Product>>{
        val accessory = repository.findProductsByCategory(ProductCategory.ACCESSORY.name).toList()
        return ResponseEntity.ok(accessory)
    }

    @GetMapping("")
    suspend fun getAllProducts(): ResponseEntity<List<Product>>{
        val all = repository.findAll().toList()
        return ResponseEntity.ok(all)
    }

    @PostMapping("")
    suspend fun saveProduct(@RequestBody dto: ProductDto):ResponseEntity<Product>{
        val product = dto.toProduct()
        val created = repository.save(product)
        return ResponseEntity.ok(created)
    }
}