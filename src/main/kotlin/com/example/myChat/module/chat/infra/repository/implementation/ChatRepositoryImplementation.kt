package com.example.myChat.module.chat.infra.repository.implementation

import com.example.myChat.core.infra.repository.database.UsersDatabase
import com.example.myChat.module.chat.domain.entities.UserChat
import com.example.myChat.module.chat.domain.repository.ChatRepository
import com.example.myChat.module.chat.infra.repository.database.ChatSessionDatabase
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class ChatRepositoryImplementation : ChatRepository {
    override fun getChatSession(sendUserUUID: UUID, receiptUserUUID: UUID): UUID? = transaction {
        ChatSessionDatabase.select {
            ((ChatSessionDatabase.firstUserUUID eq sendUserUUID) and
                    (ChatSessionDatabase.secondUserUUID eq receiptUserUUID)) or
                    ((ChatSessionDatabase.firstUserUUID eq receiptUserUUID) and
                            (ChatSessionDatabase.secondUserUUID eq sendUserUUID))
        }.map {
            it[ChatSessionDatabase.uuid]
        }.firstOrNull()
    }

    override fun createChatSession(sendUserUUID: UUID, receiptUserUUID: UUID): UUID? = transaction {
        val sessionUUID = UUID.randomUUID()
        ChatSessionDatabase.insert {
            it[uuid] = sessionUUID
            it[firstUserUUID] = sendUserUUID
            it[secondUserUUID] = receiptUserUUID
            it[createdAt] = LocalDateTime.now()
        }
        sessionUUID
    }

    override fun getUserChatByUUID(userChatUUID: UUID): UserChat? = transaction {
        UsersDatabase.select(Op.build { UsersDatabase.uuid eq userChatUUID }).map {
            UserChat(
                uuid = it[UsersDatabase.uuid],
                login = it[UsersDatabase.login],
                email = it[UsersDatabase.email],
                avatar = binaryToString(it[UsersDatabase.avatar]),
                altAvatar = it[UsersDatabase.altAvatar],
                name = it[UsersDatabase.name],
                surname = it[UsersDatabase.surname]
            )
        }.firstOrNull()
    }

    private fun binaryToString(a: ExposedBlob?): String? = a?.let { String(it.bytes) }
}