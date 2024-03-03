package com.example.myChat.module.chat.infra.repository.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object ChatSessionDatabase : Table("chat_session") {
    var uuid = uuid("uuid").uniqueIndex()
    var firstUserUUID = uuid("first_user_uuid")
    var secondUserUUID = uuid("second_user_uuid")
    var createdAt = datetime("created_at")
}