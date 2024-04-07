package com.example.myChat.module.messages.infra.webservice

import com.example.myChat.module.messages.domain.entities.Message
import com.example.myChat.module.messages.domain.repository.MessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/messages")
class MessageWebservice(
    private val messageRepository: MessageRepository
) {

    @Autowired
    lateinit var simpMessagingTemplate: SimpMessagingTemplate

    @GetMapping
    fun getMessages(
        @RequestParam(
            required = true,
            name = "chatSessionUUID",
            defaultValue = ""
        ) chatSessionUUID: UUID
    ): List<Message> = messageRepository.getMessages(chatSessionUUID)

    @PostMapping("/send_message_by_post")
    fun sendMessageByPostMapping(message: Message){
        try {
            if(message.content.isNullOrEmpty()) return;

            val newMessage = messageRepository.saveMessage(message)
            simpMessagingTemplate.convertAndSend("/topic/${message.chatSessionUUID}", newMessage)

        }catch (e: Exception){
            println("caiu aqui otario")
            println(e.message)
        }
    }
}