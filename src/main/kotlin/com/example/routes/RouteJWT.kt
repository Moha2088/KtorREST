package com.example.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.Repositories.UserRepository
import com.example.models.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

val userRepo = UserRepository()

fun Application.routeLogin(secret: String, issuer: String, audience: String){

    routing {

        post("/token") {
            val user = call.receive<User>()

            if(userRepo.getAll().find { it.id == user.id } == null) {  // Check if user with the same id already exists

                val expirationDate = Date(System.currentTimeMillis() + 3600000)  // 1 hour

                val generatedToken = JWT
                    .create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withClaim("username", user.userName)
                    .withClaim("password", user.password)
                    .withExpiresAt(expirationDate)
                    .sign(Algorithm.HMAC512(secret))

                userRepo.addUser(user)
                call.respondText("Token: $generatedToken", status = HttpStatusCode.OK)
            }

            else if (userRepo.getAll().find { it.id == user.id } != null){
                call.respondText("User with that id already exists!", status = HttpStatusCode.BadRequest)
            }
        }
    }
}