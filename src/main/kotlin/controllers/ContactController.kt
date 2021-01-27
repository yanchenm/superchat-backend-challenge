package controllers

import db.DatabaseFactory.dbQuery
import models.Contact
import models.Contacts
import models.NewContact
import org.jetbrains.exposed.sql.*

class ContactController {
    suspend fun addContact(contact: NewContact): Contact {
        var id = 0
        dbQuery {
            id = (Contacts.insert {
                it[name] = contact.name
                it[email] = contact.email
                it[created] = System.currentTimeMillis()
            } get Contacts.id)
        }

        return getContact(id)!!
    }

    suspend fun getAllContacts(): List<Contact> = dbQuery {
        Contacts.selectAll().map { toContact(it) }
    }

    suspend fun getContact(id: Int): Contact? = dbQuery {
        Contacts.select {
            Contacts.id eq id
        }.mapNotNull {
            toContact(it)
        }.singleOrNull()
    }

    suspend fun deleteContact(id: Int): Boolean = dbQuery {
        Contacts.deleteWhere { Contacts.id eq id } > 0
    }

    private fun toContact(row: ResultRow): Contact =
        Contact(
            id = row[Contacts.id],
            name = row[Contacts.name],
            email = row[Contacts.email],
            created = row[Contacts.created],
        )
}
