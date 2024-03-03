package com.example.myChat.module.chat.domain.repository

import java.util.UUID

interface ChatRepository {
    fun getChatSession(sendUserUUID: UUID, receiptUserUUID: UUID): UUID?
    fun createChatSession(sendUserUUID: UUID, receiptUserUUID: UUID): UUID?
}