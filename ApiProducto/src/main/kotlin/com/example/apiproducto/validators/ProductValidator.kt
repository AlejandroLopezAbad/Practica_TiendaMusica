package com.example.apiproducto.validators

import com.example.apiproducto.dto.ProductDto
import com.example.apiproducto.exceptions.ProductBadRequestException
import com.example.apiproducto.models.ProductCategory

fun ProductDto.validate(): ProductDto{
    if(this.name.isBlank())
        throw ProductBadRequestException("El nombre del producto no puede estar vacío")
    if(this.price < 0)
        throw ProductBadRequestException("El precio no puede ser negativo")
    if(this.description.isBlank())
        throw ProductBadRequestException("La descripción no puede estar vacía")
    if(this.category.isBlank())
        throw ProductBadRequestException("La categoría no puede estar vacía")
    if(!ProductCategory.values().map{it.toString()}.contains(this.category.uppercase()))
        throw ProductBadRequestException("La categoría es incorrecta")
    if (this.stock < 0)
        throw ProductBadRequestException("El stock no puede ser negativo")
    if (this.brand.isBlank())
        throw ProductBadRequestException("La marca no puede estar vacía")
    if (this.model.isBlank())
        throw ProductBadRequestException("El modelo no puede estar vacío")

    return this
}