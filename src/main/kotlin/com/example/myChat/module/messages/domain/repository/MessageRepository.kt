package com.example.myChat.module.messages.domain.repository

import com.example.myChat.module.messages.domain.entities.Message
import java.util.UUID

interface MessageRepository {
    fun getMessages(chatSessionUUID: UUID): List<Message>
    fun saveMessage(message: Message): Message
    fun validateUserInChatSession(userUUID: UUID, chatSessionUUID: UUID): Boolean
}