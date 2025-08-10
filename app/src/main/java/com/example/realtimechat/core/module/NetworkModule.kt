package com.example.realtimechat.core.module


import com.example.internet_connection_monitor.network.InternetConnectionMonitor
import com.example.internet_connection_monitor.network.InternetConnectionMonitorImpl
import com.example.realtimechat.core.logger.Logger
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single<InternetConnectionMonitor> { InternetConnectionMonitorImpl(get()) }
    single {
        val reporterLogger: Logger = get()
        HttpClient(OkHttp.create()) {
            install(Logging) {
                level = LogLevel.BODY
                logger = object : io.ktor.client.plugins.logging.Logger {
                    override fun log(message: String) {
                        reporterLogger.d(message)
                    }
                }
            }
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }
}