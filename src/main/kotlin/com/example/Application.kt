package com.example

import com.example.plugins.*
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.config.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

val config = HoconApplicationConfig(ConfigFactory.load())
val secret = config.property("jwt.secret").getString()
val issuer = config.property("jwt.issuer").getString()
val audience = config.property("jwt.audience").getString()
var myRealm = config.property("jwt.realm").getString()

fun Application.module() {
    configureSerialization()
    configureSecurity(secret = secret, issuer = issuer, audience = audience, myRealm = myRealm)
    configureRouting(secret = secret, issuer = issuer, audience = audience)
}