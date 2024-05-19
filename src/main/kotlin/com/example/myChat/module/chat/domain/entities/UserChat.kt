package com.example.myChat.module.chat.domain.entities

import java.util.UUID

class UserChat(
    val uuid: UUID? = null,
    val login: String? = "",
    val email: String? = "",
    val avatar: String? = null,
    val altAvatar: String? = "",
    val name: String? = null,
    val surname: String? = null
)