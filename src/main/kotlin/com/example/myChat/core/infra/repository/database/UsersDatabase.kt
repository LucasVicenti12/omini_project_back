package com.example.myChat.core.infra.repository.database

import org.jetbrains.exposed.sql.Table

object UsersDatabase : Table("users") {
    var uuid = uuid("uuid").uniqueIndex()
    var login = varchar("login", 60).uniqueIndex()
    var password = text("password")
    var userType = text("user_type")
    var email = varchar("email", 90).nullable()
    var avatar = blob("avatar_image").nullable()
    var altAvatar = char("alt_avatar", 2)
    var userStatus = integer("user_status")
    var name = varchar("name", 20)
    var surname = varchar("surname", 50)
}