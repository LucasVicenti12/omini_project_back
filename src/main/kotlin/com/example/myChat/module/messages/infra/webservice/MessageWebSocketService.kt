package com.example.myChat.module.messages.infra.webservice

import com.example.myChat.module.messages.domain.entities.Message
import com.example.myChat.module.messages.domain.repository.MessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class MessageWebSocketService(
    private val messageRepository: MessageRepository
) {

    @Autowired
    lateinit var simpMessagingTemplate: SimpMessagingTemplate

    @MessageMapping("/chat_add_message")
    fun addMessage(@Payload message: Message) {
        if(message.content.isNullOrEmpty()) return

        val newMessage = messageRepository.saveMessage(message)
        simpMessagingTemplate.convertAndSend("/topic/${message.chatSessionUUID}", newMessage)
    }
}