package com.example

import io.github.smiley4.ktoropenapi.OpenApi
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    // OpenAPIプラグイン
    install(OpenApi) {
        info {
            title = "Sample API"
            version = "1.0.0"
            description = "API Documentation"
        }
        server {
            url = "http://localhost:8080"
            description = "Development Server"
        }
    }

    configureHTTP()  // 既存のCORS設定
    configureRouting()
}