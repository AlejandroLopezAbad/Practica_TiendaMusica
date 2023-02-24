package com.example.apiproducto.services


import com.example.apiproducto.exceptions.*
import com.example.apiproducto.models.Product
import com.example.apiproducto.repositories.ProductRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
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

    @Cacheable("products")
    suspend fun findProductById(id:Int):Product?{
        return repository.findById(id)
            ?: throw ProductNotFoundException("No se ha encontrado un producto con el id: $id")
    }

    @Cacheable("products")
    suspend fun findProductByUuid(uuid:String): Product?{
        return repository.findProductByUuid(uuid).firstOrNull()
            ?: throw ProductNotFoundException("No se ha encontrado un producto con el uuid: $uuid")
    }

    @Cacheable("products")
    suspend fun saveProduct(product: Product): Product{
        return repository.save(product)
    }

    @Cacheable("products")
    suspend fun updateProduct(product: Product, updateData:Product): Product{
        return repository.save(Product(
                id = product.id,
                uuid = product.uuid,
                name = updateData.name,
                price = updateData.price,
                available = updateData.available,
                description = updateData.description,
                url = updateData.url,
                category = updateData.category,
                stock = updateData.stock,
                brand = updateData.brand,
                model = updateData.model
        ))
    }


    suspend fun deleteProduct(product: Product){
        repository.delete(product)
    }
}