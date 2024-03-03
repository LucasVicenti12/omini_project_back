package com.example.myChat.module.chat.infra.webservice

import com.example.myChat.module.chat.domain.usecase.response.ChatSessionResponse
import java.util.*

interface ChatWebservice {
    fun connectChat(sendUserUUID: UUID, receiptUserUUID: UUID): ChatSessionResponse
}