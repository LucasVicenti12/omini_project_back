package com.example.myChat.core.domain.entities

class Register (
    val login: String? = "",
    val password: String? = "",
    val userTypes: UserTypes? = UserTypes.USER
)