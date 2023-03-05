package es.tiendamusica.validation

import es.tiendamusica.dtos.OrderCreateDto
import es.tiendamusica.dtos.OrderUpdateDto
import io.ktor.server.plugins.requestvalidation.*

fun RequestValidationConfig.orderValidators(){
    validate<OrderCreateDto>{ dto ->
    if (dto.price <=0){
        ValidationResult.Invalid(" El precio del pedido no puede ser menor o igual a 0")
    }else if (dto.userId.isBlank()){
        ValidationResult.Invalid(" El id del usuario  no puede estar en blanco")
    }else if(dto.products.isEmpty()){
        ValidationResult.Invalid(" La lista de productos no puede estar vacía")
    }
    else{
        ValidationResult.Valid
    }
    }
    validate<OrderUpdateDto>{ dto ->
        if(dto.price!! <= 0){
            ValidationResult.Invalid("El precio del pedido no puese der menor o igual a 0")

        }else {
            ValidationResult.Valid
        }
    }
}