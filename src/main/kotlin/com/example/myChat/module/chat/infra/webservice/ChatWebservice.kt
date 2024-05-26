package com.example.myChat.module.chat.infra.webservice

import com.example.myChat.module.chat.domain.usecase.response.ChatSessionResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import java.util.*

interface ChatWebservice {
    fun connectChat(receiptUserUUID: UUID, request: HttpServletRequest): ResponseEntity<ChatSessionResponse>?
}