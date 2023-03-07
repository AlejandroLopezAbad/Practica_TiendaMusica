package com.example.plugins

import com.example.service.retrofit.RetroFitClient
import com.example.service.retrofitOrder.RetroFitClientOrder
import com.example.service.retrofit.RetroFitRest
import com.example.service.retrofitOrder.RetroFitRestPedidos
import io.ktor.server.application.*
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        defaultModule()
        modules(moduleApp)
        modules(moduleApp2)
        modules(moduleApp3)
    }
}

val moduleApp = module {
    single<RetroFitRest>(named("apiProduct")) { RetroFitClient().getInstance(RetroFitClient.API_PRODUCT) }

}

val moduleApp2 = module {
    single<RetroFitRestPedidos>(named("apiOrder")) { RetroFitClientOrder().getInstance(RetroFitClientOrder.API_ORDER) }

}

val moduleApp3 = module {
    single<RetroFitRest>(named("apiUsuarios")) { RetroFitClient().getInstance(RetroFitClient.API_USERS) }

}