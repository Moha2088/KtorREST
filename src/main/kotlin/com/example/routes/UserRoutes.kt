package com.example.routes

import com.example.Repositories.UserRepository
import com.example.models.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class UserRoutes

fun Route.userRouting(userRepo: UserRepository = UserRepository()) {
    route("/users") {
        post {
            val user = call.receive<User>()
            if (userRepo.getAll().find { it.id == user.id } == null) {
                userRepo.addUser(user)
                call.respondText("User: ${user.userName} has been created", status = HttpStatusCode.Created)
            } else {
                call.respondText("User with that id already exists", status = HttpStatusCode.BadRequest)
            }
        }

        get {
            if (userRepo.getAll().isNotEmpty()) {
                call.respond(userRepo.getAll())
            } else {
                call.respondText("No users were found", status = HttpStatusCode.OK)
            }
        }

        get("{id?}") {
            val id =
                call.parameters["id"] ?: return@get call.respondText("Bad Request", status = HttpStatusCode.BadRequest)

            if (userRepo.getAll().isNotEmpty()) {
                call.respond(userRepo.getUser(id.toInt()))
            }

            else {
                call.respondText("User list is empty!", status = HttpStatusCode.OK)
            }

        }

        delete("{id?}") {
            val user = call.receive<User>()
            val id = call.parameters["id"] ?: return@delete call.respondText(
                "Bad Request",
                status = HttpStatusCode.BadRequest
            )
            userRepo.deleteUser(id.toInt()).also { deleteCount ->
                if (deleteCount == 1) {
                    call.respondText("User ${user.userName} has been deleted successfully!", status = HttpStatusCode.OK)
                } else {
                    call.respondText("User could not be deleted", status = HttpStatusCode.BadRequest)
                }
            }
        }
    }
}