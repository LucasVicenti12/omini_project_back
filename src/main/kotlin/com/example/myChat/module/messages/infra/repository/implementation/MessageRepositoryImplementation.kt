package com.example.myChat.module.messages.infra.repository.implementation

import com.example.myChat.module.messages.domain.entities.Message
import com.example.myChat.module.messages.domain.repository.MessageRepository
import com.example.myChat.module.messages.infra.repository.database.MessageDatabase
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
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
                    content = it[MessageDatabase.content],
                    sendMessageDateTime = it[MessageDatabase.sendMessageDateTime],
                    attachMessages = listOf(),
                    sendUserUUID = it[MessageDatabase.sendUserUUID]
                )
            }
    }

    override fun saveMessage(message: Message): Message = transaction {
        message.uuid = UUID.randomUUID()
        MessageDatabase.insert {
            it[uuid] = message.uuid!!
            it[chatSessionUUID] = message.chatSessionUUID!!
            it[content] = message.content!!
            it[sendMessageDateTime] = message.sendMessageDateTime!!
            it[sendUserUUID] = message.sendUserUUID!!
        }
        message
    }
}