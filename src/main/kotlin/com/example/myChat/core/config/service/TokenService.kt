package com.example.myChat.core.config.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.example.myChat.core.domain.entities.Users
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class TokenService {

    @Value("\${myChatDev.security.value.secret}")
    lateinit var secret: String

    fun generateToken(users: Users): String {
        try {
            return JWT.create()
                .withIssuer("my_dev_chat")
                .withSubject(users.login)
                .withExpiresAt(genExpirationDate())
                .sign(
                    Algorithm.HMAC256(secret)
                )
        } catch (e: JWTCreationException) {
            throw RuntimeException("Error while creating token", e)
        }
    }

    fun generateTokenCookie(token: String): ResponseCookie {
        return ResponseCookie
            .from("USER_SESSION", token)
            .path("/")
            .maxAge(2000)
            .httpOnly(true)
            .sameSite("Strict")
            .secure(true)
            .build()
    }

    fun getCleanCookie(): ResponseCookie = ResponseCookie.from("USER_SESSION").path("/").build()

    fun validateToken(token: String): String {
        return try {
            JWT.require(
                Algorithm.HMAC256(secret)
            )
                .withIssuer("my_dev_chat")
                .build()
                .verify(token)
                .subject
        } catch (e: JWTVerificationException) {
            ""
        }
    }

    private fun genExpirationDate(): Instant = LocalDateTime
        .now()
        .plusHours(2)
        .toInstant(ZoneOffset.of("-03:00"))
}