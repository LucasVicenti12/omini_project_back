package com.example.myChat.module.chat.domain.usecase.response

import com.example.myChat.module.chat.domain.entities.ChatSession
import com.example.myChat.module.chat.domain.entities.ChatSituationType

class ChatSessionResponse(
    val chatSession: ChatSession? = null,
    val situation: ChatSituationType? = ChatSituationType.CREATED,
    val error: String? = null
) 