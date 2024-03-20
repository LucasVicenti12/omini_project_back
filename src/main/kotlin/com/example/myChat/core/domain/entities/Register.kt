package com.example.myChat.core.domain.entities

class Register (
    val login: String? = "",
    val password: String? = "",
    val userTypes: UserTypes? = UserTypes.USER,
    val email: String? = "",
    val avatar: String? = null,
    val altAvatar: String? = "",
    val userStatus: Int? = 0,
    val name: String? = null,
    val surname: String? = null
)