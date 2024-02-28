package com.example.myChat.module.chat.domain.entities

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ChatMessage (
    var uuid: String?,
    var message: String? = "",
    var dateTimeMessage: String? = "",
    var userUUID: String? = null
){
    constructor() : this(uuid = null)
}