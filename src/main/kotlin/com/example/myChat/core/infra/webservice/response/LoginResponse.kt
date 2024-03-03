package com.example.myChat.core.infra.webservice.response

import java.util.UUID

class LoginResponse (
    val token: String,
    val userUUID: UUID
)