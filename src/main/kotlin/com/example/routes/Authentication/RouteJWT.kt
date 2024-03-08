package com.example.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.Repositories.UserRepository
import com.example.models.GenericResponse
import com.example.models.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

val userRepo = UserRepository()

fun Application.routeLogin(secret: String, issuer: String, audience: String) {
    routing {

        post("/token") {
            val user = call.receive<User>()

            if (userRepo.getAll().find { it.userName == user.userName } == null) {
                val expirationDate = Date(System.currentTimeMillis() + 3600000)  // 1 hour
                val generatedToken = JWT
                    .create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withClaim("userName", user.userName)
                    .withClaim("password", user.password)
                    .withExpiresAt(expirationDate)
                    .sign(Algorithm.HMAC512(secret))

                user.token = generatedToken
                userRepo.addUser(user)
                call.respond(HttpStatusCode.OK, GenericResponse(isSuccess = true, data = user))
            } else if (userRepo.getAll().find { it.userName == user.userName } != null) {
                call.respondText("User with that name already exists!", status = HttpStatusCode.BadRequest)
            }
        }

        authenticate {
            get("/token") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("userName").asString()
                val password = principal.payload.getClaim("password").asString()

                val user = User(username, password)
                call.respond(HttpStatusCode.OK, GenericResponse(isSuccess = true, data = user))
            }
        }
    }
}