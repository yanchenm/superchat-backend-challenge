import com.fasterxml.jackson.databind.SerializationFeature
import com.viartemev.ktor.flyway.FlywayFeature
import controllers.ContactController
import db.DatabaseFactory
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.util.*
import org.jetbrains.exposed.sql.Database
import org.slf4j.event.Level
import routes.contactRoutes

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }

    val db = DatabaseFactory.create()
    Database.connect(db)

    install(FlywayFeature) {
        dataSource = db
    }

    val contactController = ContactController()

    routing {
        contactRoutes(contactController)
    }
}

