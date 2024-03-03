package com.example.myChat.module.chat.domain.entities

import com.example.myChat.module.messages.domain.entities.Message
import java.time.LocalDateTime
import java.util.UUID

class Chat(
    var chatSessionUUID: UUID?,
    var messages: List<Message>? = null,
    var createdAt: LocalDateTime? = LocalDateTime.now(),
    var lastMessage: LocalDateTime? = LocalDateTime.now(),
    var sendUserUUID: UUID? = null,
    var receiptUserUUID: UUID? = null,
) {
    constructor() : this(chatSessionUUID = null)
}