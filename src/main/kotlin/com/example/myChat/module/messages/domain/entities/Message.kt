package com.example.myChat.module.messages.domain.entities

import java.time.LocalDateTime
import java.util.UUID

data class Message(
    var uuid: UUID?,
    var chatSessionUUID: UUID? = null,
    var content: String? = "",
    var dateTimeMessage: LocalDateTime? = LocalDateTime.now(),
    var attachMessage: Message? = null,
    var sendUserUUID: UUID? = null
) {
    constructor() : this(uuid = null)
}