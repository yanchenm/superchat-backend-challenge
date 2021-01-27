package models

import org.jetbrains.exposed.sql.Table

object Contacts : Table() {
    val id = integer("id").autoIncrement()
    val name = text("name")
    val email = text("email")
    val created = long("created")

    override val primaryKey = PrimaryKey(id)
}

data class Contact(val id: Int, val name: String, val email: String, val created: Long)

data class NewContact(val name: String, val email: String)
