package com.example.apiproducto.controller

import com.example.apiproducto.dto.ProductDto
import com.example.apiproducto.dto.ProductResponseDto
import com.example.apiproducto.exceptions.InvalidTokenException
import com.example.apiproducto.exceptions.ProductBadRequestException
import com.example.apiproducto.exceptions.ProductNotFoundException
import com.example.apiproducto.mappers.toProduct
import com.example.apiproducto.mappers.toProductResponseDto
import com.example.apiproducto.models.*
import com.example.apiproducto.services.ProductService
import com.example.apiproducto.services.TokenService
import com.example.apiproducto.validators.validate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/product")
class ProductController
@Autowired constructor(
    private var service: ProductService,
    private var tokenService: TokenService
) {


    @GetMapping("/guitar")
    suspend fun getGuitarProducts(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String
    ): ResponseEntity<List<ProductResponseDto>> {
        return try {
            val roles = tokenService.getRoles(token)
            val guitars = service.findProductsByCategory(ProductCategory.GUITAR.name).map {it.toProductResponseDto()}
            if(roles.contains("ADMIN") || roles.contains("SUPERADMIN") || roles.contains("EMPLEADO")){
                ResponseEntity.ok(guitars)
            }else{
                val guitarsFilter = guitars.filter { it.available }
                ResponseEntity.ok(guitarsFilter)
            }
        }catch (e: InvalidTokenException){
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.message)
        }
    }


    @GetMapping("/bass_guitar")
    suspend fun getBassGuitarProducts(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String
    ): ResponseEntity<List<ProductResponseDto>> {
        return try {
            val roles = tokenService.getRoles(token)
            val bass = service.findProductsByCategory(ProductCategory.BASS_GUITAR.name).map { it.toProductResponseDto()}
            if (roles.contains("ADMIN") || roles.contains("SUPERADMIN") || roles.contains("EMPLEADO")){
                ResponseEntity.ok(bass)
            }else{
                val bassFilter = bass.filter { it.available }
                ResponseEntity.ok(bassFilter)
            }
        }catch (e: InvalidTokenException){
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.message)
        }
    }


    @GetMapping("/booster")
    suspend fun getBoosterProducts(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String
    ): ResponseEntity<List<ProductResponseDto>> {
        return try {
            val roles = tokenService.getRoles(token)
            val booster = service.findProductsByCategory(ProductCategory.BOOSTER.name).map { it.toProductResponseDto()}
            if(roles.contains("ADMIN") || roles.contains("SUPERADMIN") || roles.contains("EMPLEADO")){
                ResponseEntity.ok(booster)
            }else{
                val boosterFilter = booster.filter { it.available }
                ResponseEntity.ok(boosterFilter)
            }
        }catch (e: InvalidTokenException){
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.message)
        }
    }


    @GetMapping("/accessory")
    suspend fun getAccessoryProducts(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String
    ): ResponseEntity<List<ProductResponseDto>> {
        return try {
            val roles = tokenService.getRoles(token)
            val accessory = service.findProductsByCategory(ProductCategory.ACCESSORY.name).map { it.toProductResponseDto() }
            if(roles.contains("ADMIN") || roles.contains("SUPERADMIN") || roles.contains("EMPLEADO")) {
                ResponseEntity.ok(accessory)
            }else{
                val accessoryFilter = accessory.filter { it.available }
                ResponseEntity.ok(accessoryFilter)
            }
        }catch (e : InvalidTokenException){
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.message)
        }
    }


    @GetMapping("")
    suspend fun getAllProducts(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String
    ): ResponseEntity<List<ProductResponseDto>> {
        return try {
            val roles = tokenService.getRoles(token)
            val all = service.findAllProducts().map { it.toProductResponseDto() }
            if(roles.contains("ADMIN") || roles.contains("SUPERADMIN") || roles.contains("EMPLEADO")){
                ResponseEntity.ok(all)
            }else{
                val allFilter = all.filter { it.available }
                ResponseEntity.ok(allFilter)
            }
        }catch (e: InvalidTokenException){
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.message)
        }
    }

//    @GetMapping("/{id}")
//    suspend fun findProductById(@PathVariable id: Int): ResponseEntity<Product> {
//        try {
//            val find = service.findProductById(id)
//            return ResponseEntity.ok(find)
//        } catch (e: ProductNotFoundException) {
//            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
//        }
//    }


    @GetMapping("/{uuid}")
    suspend fun findProductByUuid(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String,
        @PathVariable uuid: String
    ): ResponseEntity<ProductResponseDto> {
        return try {
            val roles = tokenService.getRoles(token)
            val find = service.findProductByUuid(uuid)
            if(roles.contains("ADMIN") || roles.contains("SUPERADMIN") || roles.contains("EMPLEADO")){
                ResponseEntity.ok(find.toProductResponseDto())
            }else{
                if(!find.available){
                    throw ResponseStatusException(HttpStatus.NOT_FOUND,"No se ha encontrado un producto con el uuid: $uuid")
                }else{
                    ResponseEntity.ok(find.toProductResponseDto())
                }
            }
        } catch (e: ProductNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        } catch (e: InvalidTokenException){
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.message)
        }
    }


    @PostMapping("")
    suspend fun saveProduct(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String,
        @RequestBody dto: ProductDto
    ): ResponseEntity<ProductResponseDto> {
        return try {
            val roles = tokenService.getRoles(token)
            if(roles.contains("ADMIN") || roles.contains("SUPERADMIN") || roles.contains("EMPLEADO")){
                val product = dto.validate().toProduct()
                val created = service.saveProduct(product)
                ResponseEntity.status(HttpStatus.CREATED).body(created.toProductResponseDto())
            }else{
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED,"No tienes permisos para realizar esta accion")
            }
        } catch (e: ProductBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: InvalidTokenException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.message)
        }
    }


    @PutMapping("/{uuid}")
    suspend fun updateProduct(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String,
        @RequestBody dto: ProductDto,
        @PathVariable uuid: String,
    ): ResponseEntity<ProductResponseDto> {
        return try {
            val roles = tokenService.getRoles(token)
            if(roles.contains("ADMIN") || roles.contains("SUPERADMIN") || roles.contains("EMPLEADO")){
                val find = service.findProductByUuid(uuid)
                val dtoProduct = dto.validate().toProduct()
                val updated = service.updateProduct(find, dtoProduct)
                ResponseEntity.ok(updated.toProductResponseDto())
            }else{
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tienes permisos para realizar esta acción")
            }
        } catch (e: ProductNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        } catch (e: ProductBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: InvalidTokenException){
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.message)
        }
    }


    @DeleteMapping("/{uuid}")
    suspend fun deleteProduct(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String,
        @PathVariable uuid: String
    ): ResponseEntity<ProductResponseDto> {
        return try {
            val roles = tokenService.getRoles(token)
            if(roles.contains("SUPERADMIN")){
                service.deleteProduct(uuid)
                ResponseEntity.noContent().build()
            }else if(roles.contains("ADMIN")){
                service.notAvailableProduct(uuid)
                ResponseEntity.noContent().build()
            }else{
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tienes permisos para realizar esta acción")
            }
        } catch (e: ProductNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        } catch (e: InvalidTokenException){
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.message)
        }
    }
}