package com.example.apiproducto.services

import com.example.apiproducto.models.Product
import com.example.apiproducto.repositories.ProductRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductService
@Autowired constructor(
    private val repository: ProductRepository
){

    suspend fun findProductsByCategory(category: String):List<Product>{
        return repository.findProductsByCategory(category).toList()
    }

    suspend fun findAllProducts():List<Product>{
        return repository.findAll().toList()
    }

    suspend fun findProductById(id:Int):Product?{
        return repository.findById(id)
    }

    suspend fun findProductByUuid(uuid:String): Product?{
        return repository.findProductByUuid(uuid).firstOrNull()
    }

    suspend fun saveProduct(product: Product): Product{
        return repository.save(product)
    }

    suspend fun updateProduct(product: Product): Product{
        return repository.save(product)
    }

    suspend fun deleteProduct(product: Product){
        repository.delete(product)
    }
}