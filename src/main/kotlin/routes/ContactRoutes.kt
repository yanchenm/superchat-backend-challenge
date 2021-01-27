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
        get("{id}") {
            val id = call.parameters["id"]?.toInt() ?: return@get call.respondText(
                "Missing or invalid id",
                status = HttpStatusCode.BadRequest
            )

            val contact = contactController.getContact(id) ?: return@get call.respond(HttpStatusCode.NotFound)
            call.respond(contact)
        }
        post {
            val contact = call.receive<NewContact>()
            call.respond(HttpStatusCode.Created, contactController.addContact(contact))
        }
        delete("{id}") {
            val id = call.parameters["id"]?.toInt() ?: return@delete call.respondText(
                "Missing or invalid id",
                status = HttpStatusCode.BadRequest
            )

            val deleted = contactController.deleteContact(id)
            if (deleted) call.respond(HttpStatusCode.OK) else call.respond(HttpStatusCode.NotFound)
        }
    }
}