package com.example.myChat.module.chat.domain.entities

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ChatMessage(
    var message: String? = "",
    var dateTimeMessage: String? = "",
    var sendUserUUID: String? = null,
    var receiptUserUUID: String? = null
)