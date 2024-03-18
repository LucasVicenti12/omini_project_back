package com.example.myChat.core.infra.repository.implementation

import com.example.myChat.core.domain.entities.UserTypes
import com.example.myChat.core.domain.entities.Users
import com.example.myChat.core.domain.repository.UserRepository
import com.example.myChat.core.infra.repository.database.UsersDatabase
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64Encoder
import org.springframework.stereotype.Service
import java.sql.Blob
import java.util.*
import javax.sql.rowset.serial.SerialBlob

@Service
class UserRepositoryImplementation : UserRepository {
    override fun findByLogin(login: String): Users? = transaction {
        UsersDatabase.select { Op.build { UsersDatabase.login eq login } }.map {
            Users(
                    uuid = it[UsersDatabase.uuid],
                    login = it[UsersDatabase.login],
                    passwordUser = it[UsersDatabase.password],
                    userType = UserTypes.fromValue(it[UsersDatabase.userType])
            )
        }.firstOrNull()
    }

    override fun saveUser(users: Users): Users? = transaction {
        UsersDatabase.insert {
            it[uuid] = users.uuid!!
            it[login] = users.login!!
            it[password] = users.passwordUser!!
            it[userType] = users.userType.toString()
            it[email] = users.email!!
            if (users.avatar != null) {
                val decodeAvatarByte = Base64.getDecoder().decode(users.avatar)
                it[avatar] = decodeAvatarByte
            }
            it[altAvatar] = users.login.substring(0, 2)
            it[userStatus] = 0
        }.resultedValues?.map {
            Users(
                    uuid = it[UsersDatabase.uuid],
                    login = it[UsersDatabase.login],
                    passwordUser = it[UsersDatabase.password],
                    userType = UserTypes.fromValue(it[UsersDatabase.userType]),
                    email = it[UsersDatabase.email],
                    avatar = binaryToString(it[UsersDatabase.avatar]),
                    userStatus = it[UsersDatabase.userStatus],
                    altAvatar = it[UsersDatabase.altAvatar]
            )
        }?.firstOrNull()
    }

    override fun listUsers(): List<Users>? = transaction {
        UsersDatabase.selectAll().map {
            Users(
                    uuid = it[UsersDatabase.uuid],
                    login = it[UsersDatabase.login],
                    passwordUser = null,
                    userType = UserTypes.fromValue(it[UsersDatabase.userType])
            )
        }
    }

    override fun getUserByUUID(userUUID: UUID): Users? = transaction {
        UsersDatabase.select(Op.build { UsersDatabase.uuid eq userUUID }).map {
            Users(
                    uuid = it[UsersDatabase.uuid],
                    login = it[UsersDatabase.login],
                    passwordUser = null,
                    userType = UserTypes.fromValue(it[UsersDatabase.userType])
            )
        }.firstOrNull()
    }

    private fun binaryToString(a: ByteArray?): String{
        return a.toString()
    }
}