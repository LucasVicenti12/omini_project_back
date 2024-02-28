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
import org.springframework.stereotype.Service
import java.util.*

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
        }
        users
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
}