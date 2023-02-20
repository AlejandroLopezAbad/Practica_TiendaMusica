package com.example.apiproducto.controller

import com.example.apiproducto.dto.ProductDto
import com.example.apiproducto.exceptions.ProductNotFoundException
import com.example.apiproducto.mappers.toProduct
import com.example.apiproducto.models.*
import com.example.apiproducto.services.ProductService
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

//TODO Hacer --> Dependiendo de si es admin o no se muestran los productos no disponibles también
// TODO Cuando esté la seguridad dependiendo de si es user o admin devolver un dto
@RestController
@RequestMapping("/api/product")
class ProductController
@Autowired constructor(
    private var service: ProductService
){

    @GetMapping("/guitar")
    suspend fun getGuitarProducts(): ResponseEntity<List<Product>>{
        val guitars = service.findProductsByCategory(ProductCategory.GUITAR.name)
        return ResponseEntity.ok(guitars)
    }

    @GetMapping("/bass_guitar")
    suspend fun getBassGuitarProducts(): ResponseEntity<List<Product>>{
        val bass = service.findProductsByCategory(ProductCategory.BASS_GUITAR.name)
        return ResponseEntity.ok(bass)
    }

    @GetMapping("/booster")
    suspend fun getBoosterProducts(): ResponseEntity<List<Product>>{
        val booster = service.findProductsByCategory(ProductCategory.BOOSTER.name)
        return ResponseEntity.ok(booster)
    }

    @GetMapping("/accessory")
    suspend fun getAccessoryProducts(): ResponseEntity<List<Product>>{
        val accessory = service.findProductsByCategory(ProductCategory.ACCESSORY.name)
        return ResponseEntity.ok(accessory)
    }

    @GetMapping("")
    suspend fun getAllProducts(): ResponseEntity<List<Product>>{
        val all = service.findAllProducts()
        return ResponseEntity.ok(all)
    }

    @GetMapping("/{id}")
    suspend fun findProductById(@PathVariable id: Int): ResponseEntity<Product>{
        try {
            val find = service.findProductById(id)
            return ResponseEntity.ok(find)
        } catch (e: ProductNotFoundException){
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message!!)
        }
    }

    @PostMapping("")
    suspend fun saveProduct(@RequestBody dto: ProductDto):ResponseEntity<Product>{
        val product = dto.toProduct()
        val created = service.saveProduct(product)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }


    @PutMapping("/{id}")
    suspend fun updateProduct(
        @RequestBody dto: ProductDto,
        @PathVariable id: Int
    ): ResponseEntity<Product>{
        try {
            val find = service.findProductById(id)
            val dtoProduct = dto.toProduct()
            val updated = service.updateProduct(find!!, dtoProduct)
            return ResponseEntity.ok(updated)
        }catch (e: ProductNotFoundException){
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        }
    }

    @DeleteMapping("/{id}")
    suspend fun deleteProduct(@PathVariable id:Int): ResponseEntity<Product> {
        try {
            val find = service.findProductById(id)
            service.deleteProduct(find!!)
            return ResponseEntity.noContent().build()
        }catch (e: ProductNotFoundException){
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        }
    }
}