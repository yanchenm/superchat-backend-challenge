package models

import org.jetbrains.exposed.sql.Table

enum class MessageType {
    SENT, RECEIVED
}

object Messages : Table() {
    val id = integer("id").autoIncrement()
    val type = customEnumeration(
        "type",
        "ENUM('SENT', 'RECEIVED')",
        { value -> MessageType.valueOf(value as String) },
        { it.name })
    val contact = reference("contact", Contacts.email)
    val body = text("body")
    val sent = long("sent")

    override val primaryKey = PrimaryKey(id)
}

data class Message(val id: Int, val contact: String, val type: MessageType, val body: String, val sent: Long)

data class NewMessage(val contact: String, val body: String)
