package com.example.myChat.core.infra.webservice.response

import com.example.myChat.core.domain.entities.Users

class UsersListResponse (
    val users: List<Users>? = listOf(),
    val error: String? = null
)