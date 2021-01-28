# Superchat Backend Challenge

This is a simple API replicating the core functionality of a communications platform. It includes the following core
functionality:

- Create contacts given their name and email
- List all contacts
- Send a message to a contact
- List all previous conversations
- Receive messages from an external service via a webhook
- Automatically substitute placeholder text in messages

## Tech Stack

- Kotlin
- Ktor (web framework)
- H2 (in-memory database)
- Exposed (type-safe SQL framework)

## Endpoints

### Contacts

`POST /contacts` - Create a new contact

- Request body:

```json
{
  "name": "Yanchen Ma",
  "email": "yanchen@example.com"
}
```

- Response: 201 CREATED

```json
{
  "name": "Yanchen Ma",
  "email": "yanchen@example.com",
  "created": 1611806692403
}
```

---

`GET /contacts` - List all contacts

- Request body: None
- Response: 200 OK

```json
[
  {
    "name": "Yanchen Ma",
    "email": "yanchen@example.com",
    "created": 1611806692403
  },
  ...
]
```

---

`GET /contacts/{email}` - Get contact with given email

- Request body: None
- Response: 200 OK / 404 NOT FOUND

```json
{
  "name": "Yanchen Ma",
  "email": "yanchen@example.com",
  "created": 1611806692403
}
```

---

`DELETE /contacts/{email}` - Delete contact with given email

- Request body: None
- Response: 200 OK / 404 NOT FOUND

### Messages

`POST /messages` - Send message to contact

- Request body:
    - Wrap any placeholders in `${ }`.
    - Currently supported placeholders are `name` and `btc`

```json
{
  "contact": "yanchen@example.com",
  "body": "Hi ${name}, the current price of bitcoin is ${btc}."
}
```

- Response: 201 CREATED

```json
{
  "id": 1,
  "contact": "yanchen@example.com",
  "type": "SENT",
  "body": "Hi Yanchen Ma, the current price of bitcoin is 31455.75.",
  "sent": 1611807391337
}
```

---

`POST /receive` - Receive message from external service

- Request body:
    - If given email already exists in system, use existing contact
    - Otherwise, create a new contact with the given information
    - Placeholders are not supported

```json
{
  "email": "other@example.com",
  "name": "John Smith",
  "message": "Hello there"
}
```

- Resposne: 200 OK

```json
{
  "name": "John Smith",
  "email": "other@example.com",
  "message": "Hello there"
}
```

---

`GET /messages` - List all past conversations

- Request body: None
- Response: 200 OK

```json
[
  {
    "id": 2,
    "contact": "other@example.com",
    "type": "RECEIVED",
    "body": "Hello there",
    "sent": 1611797303604
  },
  {
    "id": 1,
    "contact": "yanchen@example.com",
    "type": "SENT",
    "body": "Hi Yanchen Ma, the current price of bitcoin is 31455.75.",
    "sent": 1611797298836
  }
]
```

---

`GET /messages/{email}` - Get all messages with a particular contact

- Request body: None
- Response: 200 OK

```json
[
  {
    "id": 1,
    "contact": "yanchen@example.com",
    "type": "SENT",
    "body": "Hi Yanchen Ma, the current price of bitcoin is 31455.75.",
    "sent": 1611797298836
  }
]
```

---

`GET /message/{id}` - Get message by ID

- Request body: None
- Response: 200 OK / 404 NOT FOUND

```json
{
  "id": 2,
  "contact": "other@example.com",
  "type": "RECEIVED",
  "body": "Hello there",
  "sent": 1611797303604
}
```

## Running Locally

To run the API locally, simply clone the repo and run:

```
docker-compose up
```

The server will start on `http://localhost:8080/`.