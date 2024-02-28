package com.example.myChat.module.chat.infra.handler

import com.example.myChat.module.chat.domain.entities.ChatMessage
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.time.LocalDateTime

class WebSocketHandler : TextWebSocketHandler() {

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val userUUID = getUserUUIDFromSession(session)
        sendMessageToChannel(message, session)
    }

    private fun getUserUUIDFromSession(session: WebSocketSession): String {
        val uri = session.uri.toString()
        return uri.substring(uri.lastIndexOf("/") + 1)
    }

    private fun sendMessageToChannel(message: TextMessage, session: WebSocketSession) {
        val chatMessage = message.payload.fromJSON<ChatMessage>()

        chatMessage.dateTimeMessage = LocalDateTime.now().toString()

        session.sendMessage(TextMessage(chatMessage.toJSON().toString()))
    }
}

inline fun <reified T> String.fromJSON(): T = Json.decodeFromString(this)
inline fun <reified T> T.toJSON(): JsonElement = Json.encodeToJsonElement(this)
