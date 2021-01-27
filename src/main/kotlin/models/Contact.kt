package models

import org.jetbrains.exposed.sql.Table

object Contacts : Table() {
    val email = varchar("email", 256)
    val name = varchar("name", 256)
    val created = long("created")

    override val primaryKey = PrimaryKey(email)
}

data class Contact(val name: String, val email: String, val created: Long)

data class NewContact(val name: String, val email: String)
