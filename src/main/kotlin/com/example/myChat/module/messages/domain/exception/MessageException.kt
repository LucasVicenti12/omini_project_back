package com.example.myChat.module.messages.domain.exception

import com.example.myChat.core.domain.entities.GenericException

val UNAUTHORIZED = MessageException("UNAUTHORIZED", "Unauthorized user")
val SERVER_ERROR = MessageException("SERVER_ERROR", "An unexpected error has occurred")

class MessageException(code: String, description: String) : GenericException(code, description)