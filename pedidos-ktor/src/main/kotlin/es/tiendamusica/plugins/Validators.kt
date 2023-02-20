package es.tiendamusica.plugins

import es.tiendamusica.validation.orderValidators
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configuteValidations(){
    install(RequestValidation){
        orderValidators()
    }

}