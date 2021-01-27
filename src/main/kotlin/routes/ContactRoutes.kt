package routes

import controllers.ContactController
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import models.NewContact

fun Route.contactRoutes(contactController: ContactController) {
    route("/contacts") {
        get {
            call.respond(contactController.getAllContacts())
        }
        get("{email}") {
            val email = call.parameters["email"] ?: return@get call.respondText(
                "Missing or invalid email",
                status = HttpStatusCode.BadRequest
            )

            val contact = contactController.getContact(email) ?: return@get call.respond(HttpStatusCode.NotFound)
            call.respond(contact)
        }
        post {
            val contact = call.receive<NewContact>()
            try {
                val created = contactController.addContact(contact)
                call.respond(HttpStatusCode.Created, created)
            } catch (e: Exception) {
                call.respondText(e.message ?: "error", status = HttpStatusCode.InternalServerError)
            }
        }
        delete("{email}") {
            val email = call.parameters["email"] ?: return@delete call.respondText(
                "Missing or invalid email",
                status = HttpStatusCode.BadRequest
            )

            val deleted = contactController.deleteContact(email)
            if (deleted) call.respond(HttpStatusCode.OK) else call.respond(HttpStatusCode.NotFound)
        }
    }
}