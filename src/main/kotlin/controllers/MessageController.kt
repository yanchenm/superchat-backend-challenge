package controllers

import db.DatabaseFactory.dbQuery
import models.Contact
import models.Message
import models.MessageType
import models.Messages
import org.jetbrains.exposed.sql.*

class MessageController {

    private val contactController = ContactController()

    suspend fun sendMessage(email: String, message: String): Message {
        val recipient = contactController.getContact(email) ?: throw Exception("Recipient does not exist")
        var id: Int? = -1

        dbQuery {
            id = Messages.insert {
                it[type] = MessageType.SENT
                it[contact] = email
                it[body] = parseMessageBody(recipient, message)
                it[sent] = System.currentTimeMillis()
            } get Messages.id
        }

        return getMessage(id!!)!!
    }

    suspend fun getMessage(id: Int): Message? = dbQuery {
        Messages.select {
            Messages.id eq id
        }.mapNotNull {
            toMessage(it)
        }.singleOrNull()
    }

    suspend fun getAllMessages(): List<Message> = dbQuery {
        Messages.selectAll().orderBy(Messages.sent to SortOrder.DESC).map { toMessage(it) }
    }

    suspend fun getAllMessagesForContact(email: String): List<Message> = dbQuery {
        Messages.select {
            Messages.contact eq email
        }.orderBy(
            Messages.sent to SortOrder.DESC
        ).map { toMessage(it) }
    }

    private fun parseMessageBody(contact: Contact, message: String): String {
        val placeholderPattern = "\\$\\{(\\S+)}".toRegex()
        return message.replace(placeholderPattern) {
            when (it.groupValues[1]) {
                "name" -> contact.name
                "btc" -> 123.toString()
                else -> it.value
            }
        }
    }

    private fun toMessage(row: ResultRow): Message =
        Message(
            id = row[Messages.id],
            type = row[Messages.type],
            contact = row[Messages.contact],
            body = row[Messages.body],
            sent = row[Messages.sent],
        )
}