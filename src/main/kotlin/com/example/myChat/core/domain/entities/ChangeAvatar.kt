package com.example.myChat.core.domain.entities

import java.util.UUID

class ChangeAvatar(
        val userUUID: UUID,
        val newAvatar: String
)