package com.example.myChat.module.chat.infra.webservice.implementation

import com.example.myChat.module.chat.domain.usecase.ChatUseCase
import com.example.myChat.module.chat.domain.usecase.response.ChatSessionResponse
import com.example.myChat.module.chat.infra.webservice.ChatWebservice
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/chat_session")
class ChatWebserviceImplementation(
    private val chatUseCase: ChatUseCase
) : ChatWebservice {

    @GetMapping("/connect")
    override fun connectChat(
        @RequestParam(
            required = true,
            name = "sendUserUUID",
            defaultValue = ""
        )
        sendUserUUID: UUID,
        @RequestParam(
            required = true,
            name = "receiptUserUUID",
            defaultValue = ""
        )
        receiptUserUUID: UUID
    ): ChatSessionResponse = chatUseCase.connectChat(sendUserUUID, receiptUserUUID)
}