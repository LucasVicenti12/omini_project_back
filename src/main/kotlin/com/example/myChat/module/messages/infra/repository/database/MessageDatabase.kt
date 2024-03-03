package com.example.myChat.module.messages.infra.repository.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object MessageDatabase : Table("messages") {
    var uuid = uuid("uuid").uniqueIndex()
    var chatSessionUUID = uuid("chat_session_uuid")
    var content = text("content").nullable()
    var sendMessageDateTime = datetime("send_message_date_time")
    var sendUserUUID = uuid("send_user_uuid")
}