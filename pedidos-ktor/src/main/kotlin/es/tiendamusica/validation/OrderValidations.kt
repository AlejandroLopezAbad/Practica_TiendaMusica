package es.tiendamusica.validation

import es.tiendamusica.dtos.OrderCreateDto
import es.tiendamusica.dtos.OrderUpdateDto
import es.tiendamusica.exceptions.OrderBadRequest
import io.ktor.server.plugins.requestvalidation.*

fun RequestValidationConfig.orderValidators(){
    validate<OrderCreateDto>{ dto ->
    if (dto.price <=0){
        throw OrderBadRequest(" El precio del pedido no puede ser menor o igual a 0")
    }else if (dto.userId.isBlank()){
        throw  OrderBadRequest(" El id del usuario  no puede estar en blanco")
    }else if(dto.products.isEmpty()){
        throw OrderBadRequest (" La lista de productos no puede estar vacía")
    }
    else{
        ValidationResult.Valid
    }
    }
    validate<OrderUpdateDto>{ dto ->
        if(dto.price!! <= 0){
            throw OrderBadRequest("El precio del pedido no puese der menor o igual a 0")

        }else {
            ValidationResult.Valid
        }
    }
}