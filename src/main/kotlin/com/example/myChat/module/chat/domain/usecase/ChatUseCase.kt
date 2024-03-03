package com.example.myChat.module.chat.domain.usecase

import com.example.myChat.module.chat.domain.usecase.response.ChatSessionResponse
import java.util.*

interface ChatUseCase {
    fun connectChat(sendUserUUID: UUID, receiptUserUUID: UUID): ChatSessionResponse
}