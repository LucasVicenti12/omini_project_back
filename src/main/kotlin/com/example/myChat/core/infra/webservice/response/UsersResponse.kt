package com.example.myChat.core.infra.webservice.response

import com.example.myChat.core.domain.entities.Users

class UsersResponse (
    val user: Users? = null,
    val error: String? = null
)