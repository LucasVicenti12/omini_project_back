package com.example.myChat.core.domain.entities

enum class UserTypes(private val type: String) {
    ADMIN("admin"),
    USER("user");

    companion object {
        fun fromValue(type: String): UserTypes = UserTypes
            .entries
            .firstOrNull() {
                it.type.equals(type, ignoreCase = true)
            } ?: throw IllegalArgumentException("Invalid user type")
    }
}