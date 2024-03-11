package com.example.routes

import com.example.Repositories.OrderRepository
import com.example.models.*
import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

class OrderRoutes

private val logger = LoggerFactory.getLogger(OrderRoutes::class.java)

fun Route.orderRouting(orderRepository: OrderRepository = OrderRepository()) {
    route("/order") {


        post {
            val order = call.receive<Order>()
            orderRepository.addOrderItem(order)
            call.respondText("Order: ${order.number} has been created")
        }

        get {
            if (orderRepository.getAll().isNotEmpty()) {
                call.respond(orderRepository.getAll())
            } else {
                call.respondText("No orders were found!", status = HttpStatusCode.OK)
            }
        }

        get("{id?}") {
            val id =
                call.parameters["id"] ?: return@get call.respondText(
                    "Bad Request",
                    status = HttpStatusCode.BadRequest
                )
            call.respond(orderRepository.getOrder(id))
        }

        get("{id?}/total") {
            val id =
                call.parameters["id"] ?: return@get call.respondText(
                    "Bad Request",
                    status = HttpStatusCode.BadRequest
                )

            val order = orderRepository.getOrder(id)
            val sum = order[0].contents.sumOf { orderInList ->
                orderInList.price * orderInList.amount
            }

            call.respondText(
                "Sum of contents in order: ${order[0].number} is: $sum kr.",
                status = HttpStatusCode.OK
            )
        }

        put("{id?}") {
            val id =
                call.parameters["id"] ?: return@put call.respondText("Bad Request", status = HttpStatusCode.BadRequest)

            val updatedOrderContents = call.receive<Order>().contents
            orderRepository.updateOrder(id, updatedOrderContents)
            call.respondText("Updated", status = HttpStatusCode.OK
            )
        }

        delete("{id?}"){
            val id = call.parameters["id"] ?: return@delete call.respondText("Bad Request", status = HttpStatusCode.BadRequest)
            orderRepository.deleteOrder(id).also { deletedCount ->
                if ( deletedCount == 1){
                    call.respondText("Order deleted", status = HttpStatusCode.OK)
                }

                else{
                    call.respondText("No orders have been deleted", status = HttpStatusCode.BadRequest)
                }
            }
        }

        delete("all") {
            orderRepository.deleteAll().also { deletedCount ->
                if (deletedCount != 0) {
                    call.respondText("All orders have been deleted.", status = HttpStatusCode.OK)
                } else {
                    call.respondText("No items have been deleted.", status = HttpStatusCode.OK)
                }
            }
        }
    }
}