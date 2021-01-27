package controllers

import db.DatabaseFactory.dbQuery
import models.Contact
import models.Contacts
import models.NewContact
import org.jetbrains.exposed.sql.*

class ContactController {
    suspend fun addContact(contact: NewContact): Contact {
        if (getContact(contact.email) != null) {
            throw Exception("Contact already exists")
        }

        dbQuery {
            Contacts.insert {
                it[email] = contact.email
                it[name] = contact.name
                it[created] = System.currentTimeMillis()
            }
        }

        return getContact(contact.email)!!
    }

    suspend fun getAllContacts(): List<Contact> = dbQuery {
        Contacts.selectAll().map { toContact(it) }
    }

    suspend fun getContact(email: String): Contact? = dbQuery {
        Contacts.select {
            Contacts.email eq email
        }.mapNotNull {
            toContact(it)
        }.singleOrNull()
    }

    suspend fun deleteContact(email: String): Boolean = dbQuery {
        Contacts.deleteWhere { Contacts.email eq email } > 0
    }

    private fun toContact(row: ResultRow): Contact =
        Contact(
            name = row[Contacts.name],
            email = row[Contacts.email],
            created = row[Contacts.created],
        )
}
