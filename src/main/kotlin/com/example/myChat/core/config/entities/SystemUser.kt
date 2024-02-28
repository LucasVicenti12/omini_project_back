package com.example.myChat.core.config.entities

import com.example.myChat.core.domain.entities.Users
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import java.util.UUID

class SystemUser(
    private val users: Users,
    private val roles: List<GrantedAuthority>
) : User(
    users.login,
    users.passwordUser,
    true,
    true,
    true,
    true,
    roles
) {
    fun getUserData(): Users = users.copy(passwordUser = null)

    val uuid: UUID
        get() = users.uuid!!
}