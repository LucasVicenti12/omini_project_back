package com.example.myChat.module.chat.domain.usecase.implementation

import com.example.myChat.module.chat.domain.entities.ChatSession
import com.example.myChat.module.chat.domain.entities.ChatSituationType
import com.example.myChat.module.chat.domain.repository.ChatRepository
import com.example.myChat.module.chat.domain.usecase.ChatUseCase
import com.example.myChat.module.chat.domain.usecase.response.ChatSessionResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChatUseCaseImplementation(
    private val chatRepository: ChatRepository
) : ChatUseCase {

    companion object {
        private var logger = LoggerFactory.getLogger(ChatUseCaseImplementation::class.java)
    }

    override fun connectChat(sendUserUUID: UUID, receiptUserUUID: UUID): ChatSessionResponse = try {
        if(sendUserUUID.equals("") || receiptUserUUID.equals("")) ChatSessionResponse(
            null,
            error = "Inform an valid values"
        )

        val chatSession = chatRepository.getChatSession(sendUserUUID, receiptUserUUID)
        println(chatSession)
        if (chatSession != null) {
            ChatSessionResponse(
                chatSession = ChatSession(chatSession),
                situation = ChatSituationType.EXISTS,
                error = null
            )
        } else {
            ChatSessionResponse(
                chatSession = ChatSession(
                    chatRepository.createChatSession(sendUserUUID, receiptUserUUID)
                ),
                situation = ChatSituationType.CREATED
            )
        }
    } catch (e: Exception) {
        logger.error("CHAT_MODULE_ERROR", e)
        ChatSessionResponse(null, error = "An unexpected error has occurred")
    }
}