package com.example.myChat.module.chat.infra.webservice.implementation

import com.example.myChat.core.config.service.TokenService
import com.example.myChat.core.domain.repository.UserRepository
import com.example.myChat.module.chat.domain.usecase.ChatUseCase
import com.example.myChat.module.chat.domain.usecase.response.ChatSessionResponse
import com.example.myChat.module.chat.infra.webservice.ChatWebservice
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/chat_session")
class ChatWebserviceImplementation(
    private val chatUseCase: ChatUseCase,
    private val userRepository: UserRepository
) : ChatWebservice {

    @Autowired
    lateinit var tokenService: TokenService

    @GetMapping("/connect")
    override fun connectChat(
        @RequestParam(
            required = true,
            name = "receiptUserUUID",
            defaultValue = ""
        )
        receiptUserUUID: UUID,
        request: HttpServletRequest
    ): ResponseEntity<ChatSessionResponse> = try {
        val cookies = request.cookies
        var userCookie = ""
        for(cookie in cookies){
            if(cookie.name.equals("USER_SESSION", ignoreCase = true)){
                userCookie = cookie.value
            }
        }
        val userLogin = tokenService.validateToken(userCookie)

        val user = userRepository.findByLogin(userLogin)

        if(user == null){
            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                    ChatSessionResponse(
                        chatSession = null,
                        situation = null,
                        "Inform an valid user"
                    )
                )
        }else{
            ResponseEntity.ok().body(chatUseCase.connectChat(user.uuid!!, receiptUserUUID))
        }

    } catch (e: Exception) {
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ChatSessionResponse(
                    chatSession = null,
                    situation = null,
                    "An unexpected error has occurred"
                )
            )
    }
}