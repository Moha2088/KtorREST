package com.example.routes

import com.example.models.*
import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


    fun Route.listCollection() {
        get("/order") {
            if (orderStorage.isNotEmpty()) {
                call.respond(orderStorage)
            }
        }
    }

    fun Route.getOrderRoute() {
        get("/order/{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText("Bad Request", status = HttpStatusCode.BadRequest)

            val order = orderStorage.find { it.number == id } ?: return@get call.respondText(
                "Order not found",
                status = HttpStatusCode.NotFound
            )

            call.respond(order)
        }
    }

    fun Route.totalizeOrderRoute() {
        get("/order/{id?}/total") {
            val id =
                call.parameters["id"] ?: return@get call.respondText("Bad Request", status = HttpStatusCode.BadRequest)
            val order = orderStorage.find { it.number == id } ?: return@get call.respondText(
                "Id not found", status = HttpStatusCode.NotFound
            )
            val total = order.contents.sumOf { it.amount * it.price }

            call.respond("The total is: $total .kr")
        }
    }