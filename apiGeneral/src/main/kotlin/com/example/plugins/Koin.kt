package com.example.plugins

import com.example.service.retrofit.RetroFitClient
import com.example.service.retrofit.RetroFitRest
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        defaultModule()
        modules(moduleApp)
    }
}

val moduleApp = module {
    single<RetroFitRest> { RetroFitClient().getInstance() }
}