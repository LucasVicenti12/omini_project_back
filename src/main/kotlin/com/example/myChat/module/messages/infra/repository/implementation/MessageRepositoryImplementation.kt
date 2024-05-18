package com.example.myChat.module.messages.infra.repository.implementation

import com.example.myChat.module.messages.domain.entities.Message
import com.example.myChat.module.messages.domain.repository.MessageRepository
import com.example.myChat.module.messages.infra.repository.database.MessageDatabase
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class MessageRepositoryImplementation : MessageRepository {
    override fun getMessages(chatSessionUUID: UUID): List<Message> = transaction {
        MessageDatabase
            .select(Op.build { MessageDatabase.chatSessionUUID eq chatSessionUUID })
            .orderBy(MessageDatabase.sendMessageDateTime)
            .map {
                Message(
                    uuid = it[MessageDatabase.uuid],
                    chatSessionUUID = it[MessageDatabase.chatSessionUUID],
                    content = binaryToString(it[MessageDatabase.content]),
                    dateTimeMessage = it[MessageDatabase.sendMessageDateTime],
                    attachMessage = getMessageByUUID(it[MessageDatabase.attachedMessageUUID]),
                    sendUserUUID = it[MessageDatabase.sendUserUUID]
                )
            }
    }

    override fun saveMessage(message: Message): Message = transaction {
        message.uuid = UUID.randomUUID()
        return@transaction MessageDatabase.insert {
            it[uuid] = message.uuid!!
            it[chatSessionUUID] = message.chatSessionUUID!!
            it[content] = ExposedBlob(message.content!!.toByteArray())
            it[sendMessageDateTime] = LocalDateTime.now()
            it[sendUserUUID] = message.sendUserUUID!!
            if (message.attachMessage != null) {
                it[attachedMessageUUID] = message.attachMessage!!.uuid!!
            }
        }.resultedValues?.map {
            Message(
                uuid = it[MessageDatabase.uuid],
                chatSessionUUID = it[MessageDatabase.chatSessionUUID],
                content = binaryToString(it[MessageDatabase.content]),
                dateTimeMessage = it[MessageDatabase.sendMessageDateTime],
                attachMessage = getMessageByUUID(it[MessageDatabase.attachedMessageUUID]),
                sendUserUUID = it[MessageDatabase.sendUserUUID]
            )
        }?.firstOrNull()!!
    }

    private fun getMessageByUUID(messageUUID: UUID?): Message? = if (messageUUID == null) {
        null
    } else {
        transaction {
            MessageDatabase.select(Op.build { MessageDatabase.uuid eq messageUUID }).map {
                Message(
                    uuid = it[MessageDatabase.uuid],
                    chatSessionUUID = it[MessageDatabase.chatSessionUUID],
                    content = binaryToString(it[MessageDatabase.content]),
                    dateTimeMessage = it[MessageDatabase.sendMessageDateTime],
                    attachMessage = null,
                    sendUserUUID = it[MessageDatabase.sendUserUUID]
                )
            }.firstOrNull()
        }
    }

    private fun binaryToString(a: ExposedBlob?): String? = a?.let { String(it.bytes) }
}