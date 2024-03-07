package com.example.routes

import com.example.Repositories.CustomerRepository
import com.example.models.Customer
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.security.MessageDigest


fun Route.customerRouting(customerRepo: CustomerRepository = CustomerRepository()) {
    route("/customer") {

        get {
            if (customerRepo.getAll().isNotEmpty()) {
                call.respond(customerRepo.getAll())
            } else {
                call.respondText("No customers were found!", status = HttpStatusCode.OK)
            }
        }

        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )

            val customer = customerRepo.getCustomer(id)
            call.respond(customer)
        }

        post {
            val customer = call.receive<Customer>()
            customerRepo.addCustomer(customer)
            call.respondText("Customer: ${customer.firstName} stored succesfully!", status = HttpStatusCode.Created)
        }

        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            customerRepo.deleteCustomer(id)
                .also {
                    call.respondText("Customer with id: $id has been deleted!", status = HttpStatusCode.OK)
                }
        }

        delete("all") {
            customerRepo.deleteAll().also {
                if (it == 0) {
                    call.respondText("No customers to delete!", status = HttpStatusCode.OK)
                } else {
                    call.respondText("All customers have been deleted!", status = HttpStatusCode.OK)
                }
            }
        }
    }
}
