package com.example.myChat.core.domain.repository

import com.example.myChat.core.domain.entities.ChangeAvatar
import com.example.myChat.core.domain.entities.Users
import java.util.UUID

interface UserRepository {
    fun findByLogin(login: String): Users?
    fun saveUser(users: Users): Users?
    fun listUsers(): List<Users>?
    fun getUserByUUID(userUUID: UUID): Users?
    fun changeAvatar(changeAvatar: ChangeAvatar): ChangeAvatar?
}