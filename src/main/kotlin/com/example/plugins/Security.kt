package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.models.GenericResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureSecurity(secret: String, issuer: String, audience: String, myRealm:String){
    install(Authentication){
        jwt {
            realm = myRealm
            
            verifier(JWT
                 .require(Algorithm.HMAC512(secret))
                .withAudience(audience)
                .withIssuer(issuer)
                .build())
            
            validate {
                jwtCredential: JWTCredential -> kotlin.run {
                    val userName = jwtCredential.payload.getClaim("userName").asString()
                    
                    if(userName.isNotEmpty()){
                        JWTPrincipal(jwtCredential.payload)
                    }
                    
                    else{
                        null
                    }
                }  
            }
            
            challenge{_, _ ->
                call.respond(HttpStatusCode.Unauthorized,GenericResponse(isSuccess = true, data ="Token is not valid, or has been expired!"))
            } 
        }
    }
}