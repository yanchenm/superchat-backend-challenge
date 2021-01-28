package routes

import controllers.MessageController
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import models.ExternalMessage
import models.NewMessage

fun Route.messageRoutes(messageController: MessageController) {
    route("/messages") {
        get {
            call.respond(messageController.getAllMessages())
        }
        get("{email}") {
            val email = call.parameters["email"] ?: return@get call.respondText(
                "Missing or invalid email",
                status = HttpStatusCode.BadRequest
            )
            call.respond(messageController.getAllMessagesForContact(email))
        }
    }
    route("/message") {
        get("{id}") {
            val id = call.parameters["id"]?.toInt() ?: return@get call.respondText(
                "Missing or invalid id",
                status = HttpStatusCode.BadRequest
            )

            val message = messageController.getMessage(id) ?: return@get call.respond(HttpStatusCode.NotFound)
            call.respond(message)
        }
        post {
            val message = call.receive<NewMessage>()
            try {
                val sent = messageController.sendMessage(message.contact, message.body)
                call.respond(HttpStatusCode.Created, sent)
            } catch (e: Exception) {
                call.respondText(e.message ?: "error", status = HttpStatusCode.InternalServerError)
            }
        }
    }
    route("/receive") {
        post {
            val message = call.receive<ExternalMessage>()
            messageController.receiveMessage(message)
            call.respond(HttpStatusCode.OK, message)
        }
    }
}