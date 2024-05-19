package com.example.myChat.module.messages.infra.webservice

import com.example.myChat.core.config.service.TokenService
import com.example.myChat.core.domain.repository.UserRepository
import com.example.myChat.module.messages.domain.entities.Message
import com.example.myChat.module.messages.domain.repository.MessageRepository
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/messages")
class MessageWebservice(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository
) {

    @Autowired
    lateinit var simpMessagingTemplate: SimpMessagingTemplate

    @Autowired
    lateinit var tokenService: TokenService

    companion object {
        private val logger = LoggerFactory.getLogger(MessageWebservice::class.java)
    }

    @GetMapping
    fun getMessages(
        @RequestParam(
            required = true,
            name = "chatSessionUUID",
            defaultValue = ""
        ) chatSessionUUID: UUID,
        request: HttpServletRequest
    ): ResponseEntity<List<Message>?> = try {
        val cookies = request.cookies
        var userCookie = ""
        for(cookie in cookies){
            if(cookie.name.equals("USER_SESSION", ignoreCase = true)){
                userCookie = cookie.value
            }
        }
        val userLogin = tokenService.validateToken(userCookie)

        val user = userRepository.findByLogin(userLogin)

        if (
            !messageRepository.validateUserInChatSession(
                user!!.uuid!!,
                chatSessionUUID
            )
        ){
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(listOf())
        }else{
            ResponseEntity.ok().body(messageRepository.getMessages(
                chatSessionUUID = chatSessionUUID, userUUID = user.uuid!!
            ))
        }
    }catch (e: Exception){
        logger.error("GET_MESSAGES", e)
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(listOf())
    }

    @PostMapping("/send_message_by_post")
    fun sendMessageByPostMapping(@RequestBody message: Message, request: HttpServletRequest) : ResponseEntity<String> = try {
        val cookies = request.cookies
        var userCookie = ""
        for(cookie in cookies){
            if(cookie.name.equals("USER_SESSION", ignoreCase = true)){
                userCookie = cookie.value
            }
        }
        val userLogin = tokenService.validateToken(userCookie)

        val user = userRepository.findByLogin(userLogin)

        if (
            !messageRepository.validateUserInChatSession(
                user!!.uuid!!,
                message.chatSessionUUID!!
            )
        ){
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user")
        }else{
            val newMessage = messageRepository.saveMessage(message)

            simpMessagingTemplate.convertAndSend(
                "/topic/${message.chatSessionUUID}",
                newMessage
            )

            ResponseEntity.ok().body("Sent message")
        }
    } catch (e: Exception) {
        logger.error("POST_MESSAGE", e)
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error has occurred")
    }
}