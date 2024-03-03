package com.example.myChat.module.messages.infra.webservice

import com.example.myChat.module.messages.domain.entities.Message
import com.example.myChat.module.messages.domain.repository.MessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller

@Controller
class MessageWebSocketService(
    private val messageRepository: MessageRepository
) {

    @Autowired
    lateinit var simpMessagingTemplate: SimpMessagingTemplate

    @MessageMapping("/chat_add_message")
    fun addMessage(@Payload message: Message) {
        messageRepository.saveMessage(message)
        simpMessagingTemplate.convertAndSend("/topic/${message.chatSessionUUID}", message)
    }
}