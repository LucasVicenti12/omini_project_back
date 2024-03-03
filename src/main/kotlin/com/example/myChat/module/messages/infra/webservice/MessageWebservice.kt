package com.example.myChat.module.messages.infra.webservice

import com.example.myChat.module.messages.domain.entities.Message
import com.example.myChat.module.messages.domain.repository.MessageRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/messages")
class MessageWebservice(
    private val messageRepository: MessageRepository
) {
    @GetMapping
    fun getMessages(
        @RequestParam(
            required = true,
            name = "chatSessionUUID",
            defaultValue = ""
        ) chatSessionUUID: UUID
    ): List<Message> = messageRepository.getMessages(chatSessionUUID)
}