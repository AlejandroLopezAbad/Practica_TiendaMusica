package com.example.apiproducto.controller

import com.example.apiproducto.dto.ProductDto
import com.example.apiproducto.dto.ProductResponseDto
import com.example.apiproducto.exceptions.InvalidTokenException
import com.example.apiproducto.exceptions.ProductBadRequestException
import com.example.apiproducto.exceptions.ProductNotFoundException
import com.example.apiproducto.exceptions.StorageBadRequestException
import com.example.apiproducto.mappers.toProduct
import com.example.apiproducto.mappers.toProductResponseDto
import com.example.apiproducto.mappers.toProductUserResponseDto
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
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String?
    ): ResponseEntity<List<Any>> {
        return try {
            token?.let{
                val roles = tokenService.getRoles(it)
                val guitars = service.findProductsByCategory(ProductCategory.GUITAR.name).map {it.toProductResponseDto()}
                if(roles.contains("ADMIN") || roles.contains("SUPERADMIN") || roles.contains("EMPLOYEE")){
                    ResponseEntity.ok(guitars)
                }else throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "El token no es válido.")
            }?: run{
                val guitars = service.findProductsByCategory(ProductCategory.GUITAR.name)
                val guitarsFilter = guitars.filter { it.available }. map{ it.toProductUserResponseDto()}
                ResponseEntity.ok(guitarsFilter)
            }
        }catch (e: InvalidTokenException){
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.message)
        }
    }


    @GetMapping("/bass_guitar")
    suspend fun getBassGuitarProducts(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String?
    ): ResponseEntity<List<Any>> {
        return try {
            token?.let{
                val roles = tokenService.getRoles(it)
                val bass = service.findProductsByCategory(ProductCategory.BASS_GUITAR.name).map {it.toProductResponseDto()}
                if(roles.contains("ADMIN") || roles.contains("SUPERADMIN") || roles.contains("EMPLOYEE")){
                    ResponseEntity.ok(bass)
                }else throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "El token no es válido.")
            }?: run{
                val bass = service.findProductsByCategory(ProductCategory.BASS_GUITAR.name)
                val bassFilter = bass.filter { it.available }. map{ it.toProductUserResponseDto()}
                ResponseEntity.ok(bassFilter)
            }
        }catch (e: InvalidTokenException){
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.message)
        }
    }


    @GetMapping("/booster")
    suspend fun getBoosterProducts(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String?
    ): ResponseEntity<List<Any>> {
        return try {
            token?.let{
                val roles = tokenService.getRoles(it)
                val booster = service.findProductsByCategory(ProductCategory.BOOSTER.name).map {it.toProductResponseDto()}
                if(roles.contains("ADMIN") || roles.contains("SUPERADMIN") || roles.contains("EMPLOYEE")){
                    ResponseEntity.ok(booster)
                }else throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "El token no es válido.")
            }?: run{
                val booster = service.findProductsByCategory(ProductCategory.BOOSTER.name)
                val boosterFilter = booster.filter { it.available }. map{ it.toProductUserResponseDto()}
                ResponseEntity.ok(boosterFilter)
            }
        }catch (e: InvalidTokenException){
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.message)
        }
    }


    @GetMapping("/accessory")
    suspend fun getAccessoryProducts(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String?
    ): ResponseEntity<List<Any>> {
        return try {
            token?.let{
                val roles = tokenService.getRoles(it)
                val accessory = service.findProductsByCategory(ProductCategory.ACCESSORY.name).map {it.toProductResponseDto()}
                if(roles.contains("ADMIN") || roles.contains("SUPERADMIN") || roles.contains("EMPLOYEE")){
                    ResponseEntity.ok(accessory)
                }else throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "El token no es válido.")
            }?: run{
                val accessory = service.findProductsByCategory(ProductCategory.ACCESSORY.name)
                val accessoryFilter = accessory.filter { it.available }. map{ it.toProductUserResponseDto()}
                ResponseEntity.ok(accessoryFilter)
            }
        }catch (e: InvalidTokenException){
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.message)
        }
    }


    @GetMapping("")
    suspend fun getAllProducts(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String?
    ): ResponseEntity<List<Any>> {
        return try {
            token?.let{
                val roles = tokenService.getRoles(it)
                val products = service.findAllProducts().map {it.toProductResponseDto()}
                if(roles.contains("ADMIN") || roles.contains("SUPERADMIN") || roles.contains("EMPLOYEE")){
                    ResponseEntity.ok(products)
                }else throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "El token no es válido.")
            }?: run{
                val products = service.findAllProducts()
                val productsFilter = products.filter { it.available }. map{ it.toProductUserResponseDto()}
                ResponseEntity.ok(productsFilter)
            }
        }catch (e: InvalidTokenException){
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.message)
        }
    }



    @GetMapping("/{uuid}")
    suspend fun findProductByUuid(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String?,
        @PathVariable uuid: String
    ): ResponseEntity<Any> {
        return try {
            token?.let {
                val roles = tokenService.getRoles(it)
                val find = service.findProductByUuid(uuid)
                if(roles.contains("ADMIN") || roles.contains("SUPERADMIN") || roles.contains("EMPLOYEE")){
                    ResponseEntity.ok(find.toProductResponseDto())
                }else throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "El token no es válido.")
            }?: run{
                val find = service.findProductByUuid(uuid)
                if(!find.available){
                    throw ResponseStatusException(HttpStatus.NOT_FOUND,"No se ha encontrado un producto con el uuid: $uuid")
                }else{
                    ResponseEntity.ok(find.toProductUserResponseDto())
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
            if(roles.contains("ADMIN") || roles.contains("SUPERADMIN") || roles.contains("EMPLOYEE")){
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
            if(roles.contains("ADMIN") || roles.contains("SUPERADMIN") || roles.contains("EMPLOYEE")){
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
        } catch (e: StorageBadRequestException){
            throw  ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }
}