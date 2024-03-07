package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import com.example.routes.*

fun Application.configureRouting(secret: String, issuer: String, audience: String) {
    routeLogin(secret = secret, issuer = issuer, audience = audience)

    routing {
        customerRouting()
        orderRouting()
        userRouting()
    }
}
