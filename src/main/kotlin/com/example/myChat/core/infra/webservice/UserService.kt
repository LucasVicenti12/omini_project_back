package com.example.myChat.core.infra.webservice

import com.example.myChat.core.config.service.TokenService
import com.example.myChat.core.domain.entities.ChangeAvatar
import com.example.myChat.core.domain.repository.UserRepository
import com.example.myChat.core.infra.webservice.response.ChangeAvatarResponse
import com.example.myChat.core.infra.webservice.response.UsersListResponse
import com.example.myChat.core.infra.webservice.response.UsersResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/users")
class UserService(
        private val userRepository: UserRepository
) {

    @Autowired
    lateinit var tokenService: TokenService

    @GetMapping("/list")
    fun getAllUsers(): UsersListResponse = try {
        UsersListResponse(users = userRepository.listUsers(), error = null)
    } catch (e: Exception) {
        UsersListResponse(error = "An unexpected error has occurred")
    }

    @GetMapping("/getUserByUUID/{userUUID}")
    fun getUserByUUID(@PathVariable("userUUID") userUUID: UUID): UsersResponse = try {
        val user = userRepository.getUserByUUID(userUUID)
        if (user != null) {
            UsersResponse(
                    user = user
            )
        } else {
            UsersResponse(error = "This user don't exists")
        }
    } catch (e: Exception) {
        UsersResponse(error = "An unexpected error has occurred")
    }

    @PostMapping("/changeAvatar")
    fun changeAvatar(@RequestBody changeAvatar: ChangeAvatar): ChangeAvatarResponse = try {
        val changeAvatarResponse = userRepository.changeAvatar(changeAvatar)
        ChangeAvatarResponse(changeAvatar = changeAvatarResponse)
    } catch (e: Exception) {
        ChangeAvatarResponse(error = "An unexpected error has occurred")
    }
}