package com.example.myChat.core.infra.repository.database

import org.jetbrains.exposed.sql.Table

object UsersDatabase : Table("users") {
    var uuid = uuid("uuid").uniqueIndex()
    var login = varchar("login", 60).uniqueIndex()
    var password = text("password")
    var userType = text("user_type")
}