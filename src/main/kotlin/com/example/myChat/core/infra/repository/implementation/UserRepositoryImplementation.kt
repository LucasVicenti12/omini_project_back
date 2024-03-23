package com.example.myChat.core.infra.repository.implementation

import com.example.myChat.core.domain.entities.ChangeAvatar
import com.example.myChat.core.domain.entities.UserTypes
import com.example.myChat.core.domain.entities.Users
import com.example.myChat.core.domain.repository.UserRepository
import com.example.myChat.core.infra.repository.database.UsersDatabase
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction
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
                it[avatar] = ExposedBlob(users.avatar.toByteArray())
            }
            it[altAvatar] = getAbreviationName(users.name!!, users.surname!!)
            it[userStatus] = 0
            it[name] = users.name
            it[surname] = users.surname
        }.resultedValues?.map {
            Users(
                    uuid = it[UsersDatabase.uuid],
                    login = it[UsersDatabase.login],
                    passwordUser = null,
                    userType = UserTypes.fromValue(it[UsersDatabase.userType]),
                    email = it[UsersDatabase.email],
                    avatar = binaryToString(it[UsersDatabase.avatar]),
                    userStatus = it[UsersDatabase.userStatus],
                    altAvatar = it[UsersDatabase.altAvatar],
                    name = it[UsersDatabase.name],
                    surname = it[UsersDatabase.surname]
            )
        }?.firstOrNull()
    }

    override fun listUsers(): List<Users>? = transaction {
        UsersDatabase.selectAll().map {
            Users(
                    uuid = it[UsersDatabase.uuid],
                    login = it[UsersDatabase.login],
                    passwordUser = null,
                    userType = UserTypes.fromValue(it[UsersDatabase.userType]),
                    email = it[UsersDatabase.email],
                    avatar = binaryToString(it[UsersDatabase.avatar]),
                    userStatus = it[UsersDatabase.userStatus],
                    altAvatar = it[UsersDatabase.altAvatar],
                    name = it[UsersDatabase.name],
                    surname = it[UsersDatabase.surname]
            )
        }
    }

    override fun getUserByUUID(userUUID: UUID): Users? = transaction {
        UsersDatabase.select(Op.build { UsersDatabase.uuid eq userUUID }).map {
            Users(
                    uuid = it[UsersDatabase.uuid],
                    login = it[UsersDatabase.login],
                    passwordUser = null,
                    userType = UserTypes.fromValue(it[UsersDatabase.userType]),
                    email = it[UsersDatabase.email],
                    avatar = binaryToString(it[UsersDatabase.avatar]),
                    userStatus = it[UsersDatabase.userStatus],
                    altAvatar = it[UsersDatabase.altAvatar],
                    name = it[UsersDatabase.name],
                    surname = it[UsersDatabase.surname]
            )
        }.firstOrNull()
    }

    override fun changeAvatar(changeAvatar: ChangeAvatar): ChangeAvatar? = transaction {
        UsersDatabase.update(
                { Op.build { UsersDatabase.uuid eq changeAvatar.userUUID } }
        ) {
            it[avatar] = ExposedBlob(changeAvatar.newAvatar.toByteArray())
        }
        changeAvatar
    }


    private fun binaryToString(a: ExposedBlob?): String? = a?.let { String(it.bytes) }
    private fun getAbreviationName(name: String, surname: String): String =
            name.uppercase().substring(0, 1) +
                    surname.uppercase().substring(0, 1)
}