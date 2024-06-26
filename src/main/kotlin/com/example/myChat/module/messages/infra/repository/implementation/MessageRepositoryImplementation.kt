package com.example.myChat.module.messages.infra.repository.implementation

import com.example.myChat.core.infra.repository.database.UsersDatabase
import com.example.myChat.module.chat.infra.repository.database.ChatSessionDatabase
import com.example.myChat.module.messages.domain.entities.Message
import com.example.myChat.module.messages.domain.entities.WhoSend
import com.example.myChat.module.messages.domain.repository.MessageRepository
import com.example.myChat.module.messages.infra.repository.database.MessageDatabase
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class MessageRepositoryImplementation() : MessageRepository {
    override fun getMessages(chatSessionUUID: UUID, userUUID: UUID, index: Int): List<Message> = transaction {
        //todo -> use index to limit messages per request
        MessageDatabase
            .join(UsersDatabase, JoinType.INNER, MessageDatabase.sendUserUUID, UsersDatabase.uuid)
            .select(Op.build { MessageDatabase.chatSessionUUID eq chatSessionUUID })
            .orderBy(MessageDatabase.sendMessageDateTime)
            .map {
                Message(
                    uuid = it[MessageDatabase.uuid],
                    chatSessionUUID = it[MessageDatabase.chatSessionUUID],
                    content = binaryToString(it[MessageDatabase.content]),
                    dateTimeMessage = it[MessageDatabase.sendMessageDateTime],
                    attachMessage = getMessageByUUID(it[MessageDatabase.attachedMessageUUID]),
                    sendUserUUID = it[MessageDatabase.sendUserUUID],
                    WhoSend(
                        isMe = it[MessageDatabase.sendUserUUID] == userUUID,
                        userName = it[UsersDatabase.login]
                    )
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
                sendUserUUID = it[MessageDatabase.sendUserUUID],
                whoSend = null
            )
        }?.firstOrNull()!!
    }

    override fun validateUserInChatSession(userUUID: UUID, chatSessionUUID: UUID): Boolean = transaction {
        ChatSessionDatabase.select {
            ((ChatSessionDatabase.firstUserUUID eq userUUID) or
                    (ChatSessionDatabase.secondUserUUID eq userUUID)) and
                    (ChatSessionDatabase.uuid eq chatSessionUUID)
        }.limit(1).count() > 0
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