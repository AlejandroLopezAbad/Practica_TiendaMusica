package com.example.apiproducto.services


import com.example.apiproducto.exceptions.*
import com.example.apiproducto.models.Product
import com.example.apiproducto.repositories.ProductRepository
import com.example.apiproducto.services.storage.StorageServiceImpl
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

/**
 * Servicio de Productos cacheado.
 * @property repository repositorio de productos.
 */
@Service
class ProductService
@Autowired constructor(
    private val repository: ProductRepository,
    private val storage: StorageServiceImpl
) {

    /**
     * Buscar productos por su categoría.
     * @param category categoría por la que buscar los productos.
     * @return lista de los productos encontrados con la categoría pedida.
     */
    suspend fun findProductsByCategory(category: String): List<Product> {
        return repository.findProductsByCategory(category).toList()
    }

    /**
     * Buscar todos los productos.
     * @return lista de todos los productos.
     */
    suspend fun findAllProducts(): List<Product> {
        return repository.findAll().toList()
    }


    /**
     * Buscar un producto por su id.
     * @param id id del producto a buscar.
     * @throws ProductNotFoundException si no ha encontrado el producto.
     * @return el producto encontrado.
     */
    @Cacheable("products")
    suspend fun findProductById(id: Int): Product {
        return repository.findById(id)
            ?: throw ProductNotFoundException("No se ha encontrado un producto con el id: $id")
    }


    /**
     * Buscar un porducto por su uuid.
     * @param uuid uuid del producto a buscar.
     * @throws ProductNotFoundException si no se ha encontrado el producto.
     * @return el producto encontrado.
     */
    @Cacheable("products")
    suspend fun findProductByUuid(uuid: String): Product {
        return repository.findProductByUuid(uuid).firstOrNull()
            ?: throw ProductNotFoundException("No se ha encontrado un producto con el uuid: $uuid")
    }


    /**
     * Guardar un producto.
     * @param product producto a guardar en el repositorio.
     * @return el producto guardado.
     */
    @CachePut("products")
    suspend fun saveProduct(product: Product): Product {
        return repository.save(product)
    }


    /**
     * Actualizar un producto.
     * @param product Producto original, el que está almacenado.
     * @param updateData producto con los datos actualizados.
     * @return producto actualizado.
     */
    @CachePut("products")
    suspend fun updateProduct(product: Product, updateData: Product): Product {
        return repository.save(
            Product(
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
            )
        )
    }


    /**
     * Actualizar la disponibilidad de un producto.
     * @param uuid uuid del producto a actualizar la disponibilidad.
     * @throws ProductNotFoundException si no se encuentra el producto a actualizar.
     * @return boolean dependiendo de sis se ha actualizado correctamente.
     */
    @CachePut("products")
    suspend fun notAvailableProduct(uuid: String): Boolean{
        val exist = repository.findProductByUuid(uuid).firstOrNull()
        exist?.let {
            val product = Product(
                id = exist.id,
                uuid = exist.uuid,
                name = exist.name,
                price = exist.price,
                available = false ,
                description = exist.description,
                url = exist.url,
                category = exist.category,
                stock = exist.stock,
                brand = exist.brand,
                model = exist.model
            )
            repository.save(product)
            return true
        } ?: throw ProductNotFoundException("No existe el producto con uuid: $uuid")
    }


    /**
     * Cambiar la url del producto.
     * @param uuid uuid del producto.
     * @param url url nueva del producto.
     * @throws ProductNotFoundException si no se encuentra el producto con ese uuid.
     * @return boolean si ha sido cambiado correctamente
     */
    @CachePut("products")
    suspend fun changeUrlProduct(uuid:String, url: String): Boolean{
        val exist = repository.findProductByUuid(uuid).firstOrNull()
        exist?.let {
            val product = Product(
                id = exist.id,
                uuid = exist.uuid,
                name = exist.name,
                price = exist.price,
                available = exist.available ,
                description = exist.description,
                url = url,
                category = exist.category,
                stock = exist.stock,
                brand = exist.brand,
                model = exist.model
            )
            repository.save(product)
            return true
        } ?: throw ProductNotFoundException("No existe el producto con uuid: $uuid")
    }


    /**
     * Cambiar la url del producto.
     * @param filename filename almacenado en el producto de la BDD.
     * @param url url nueva del producto.
     * @throws ProductNotFoundException si no se encuentra el producto con ese uuid.
     * @return boolean si ha sido cambiado correctamente
     */
    @CachePut("products")
    suspend fun deleteUrlProduct(filename:String, url: String): Boolean{
        val exist = repository.findProductByUrl(filename).firstOrNull()
        exist?.let {
            val product = Product(
                id = exist.id,
                uuid = exist.uuid,
                name = exist.name,
                price = exist.price,
                available = exist.available ,
                description = exist.description,
                url = url,
                category = exist.category,
                stock = exist.stock,
                brand = exist.brand,
                model = exist.model
            )
            repository.save(product)
            return true
        } ?: throw ProductNotFoundException("No existe el producto con url: $url")
    }


    /**
     * Eliminar un producto.
     * @param uuid uuid del producto a eliminar.
     * @throws ProductNotFoundException si no se encuentra el producto a eliminar.
     * @return boolean si se ha eliminado correctamente.
     */
    @CacheEvict("products")
    suspend fun deleteProduct(uuid: String): Boolean {
        val exist = repository.findProductByUuid(uuid).firstOrNull()
        exist?.let {
            println(it.url)
           if(it.url != "placeholder.jpg"){
               storage.deleteProduct(it.url)
           }
            return repository.delete(it).let { true }
        } ?: throw ProductNotFoundException("No existe el producto con uuid: $uuid")

    }
}