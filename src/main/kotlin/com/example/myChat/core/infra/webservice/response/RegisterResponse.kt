package com.example.myChat.core.infra.webservice.response

import com.example.myChat.core.domain.entities.UserTypes

class RegisterResponse(
    val login: String,
    val password: String,
    val userType: UserTypes
)