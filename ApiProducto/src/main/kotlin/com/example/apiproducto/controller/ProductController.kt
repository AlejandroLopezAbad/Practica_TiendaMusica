package com.example.apiproducto.controller

import com.example.apiproducto.dto.ProductDto
import com.example.apiproducto.mappers.toProduct
import com.example.apiproducto.models.*
import com.example.apiproducto.services.ProductService
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

//TODO Hacer --> Dependiendo de si es admin o no se muestran los productos no disponibles también
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
        val find = service.findProductById(id)
        find?.let {
            return ResponseEntity.ok(it)
        }?:run{
            return ResponseEntity.notFound().build()
        }
    }

    @PostMapping("")
    suspend fun saveProduct(@RequestBody dto: ProductDto):ResponseEntity<Product>{
        val product = dto.toProduct()
        val created = service.saveProduct(product)
        return ResponseEntity.ok(created)
    }

    @PutMapping("/{id}")
    suspend fun updateProduct(@PathVariable id:Int, @RequestBody dto: ProductDto): ResponseEntity<Product>{
        val find = service.findProductById(id)
        find?.let {
            val dtoProduct = dto.toProduct()
            dtoProduct.id = it.id
            dtoProduct.uuid = it.uuid
            val updated = service.updateProduct(dtoProduct)
            return ResponseEntity.ok(updated)
        }?: run{
            return ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    suspend fun deleteProduct(@PathVariable id:Int): ResponseEntity<Product> {
        val find = service.findProductById(id)
        find?.let {
            service.deleteProduct(it)
            return ResponseEntity.noContent().build()
        }?: run{
            return ResponseEntity.notFound().build()
        }
    }
}