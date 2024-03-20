package com.example.myChat.core.infra.webservice

import com.example.myChat.core.config.entities.SystemUser
import com.example.myChat.core.config.service.TokenService
import com.example.myChat.core.domain.entities.Login
import com.example.myChat.core.domain.entities.Register
import com.example.myChat.core.domain.entities.Users
import com.example.myChat.core.domain.repository.UserRepository
import com.example.myChat.core.infra.webservice.response.LoginResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("auth")
class AuthService(
        private val authenticationManager: AuthenticationManager,
        private val userRepository: UserRepository
) {
    @Autowired
    lateinit var tokenService: TokenService

    @PostMapping("/login")
    fun login(@RequestBody login: Login): ResponseEntity<LoginResponse> {
        val usernamePassword = UsernamePasswordAuthenticationToken(
                login.login,
                login.password
        )
        val auth = authenticationManager.authenticate(usernamePassword)
        val user = auth.principal as SystemUser

        val token = tokenService.generateToken(user.getUserData())

        val userUUID = user.uuid

        return ResponseEntity
                .ok()
                .header(
                        HttpHeaders.SET_COOKIE,
                        tokenService.generateTokenCookie(token).toString()
                )
                .body(LoginResponse(token = token, userUUID = userUUID))
    }

    @PostMapping("/register")
    fun saveUser(@RequestBody register: Register): Register {
        if (userRepository.findByLogin(register.login!!) != null) {
            return register
        }
        val encryptedPassword = BCryptPasswordEncoder().encode(register.password)

        val newUser = Users(
                uuid = UUID.randomUUID(),
                login = register.login,
                passwordUser = encryptedPassword,
                userType = register.userTypes,
                email = register.email,
                avatar = register.avatar,
                altAvatar = register.altAvatar,
                userStatus = register.userStatus,
                name = register.name,
                surname = register.surname
        )

        userRepository.saveUser(newUser)
        return register
    }

    @RequestMapping("/logout", method = [RequestMethod.POST, RequestMethod.GET])
    fun logout(): ResponseEntity<*>? {
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, tokenService.getCleanCookie().toString())
                .body("LOGGED OUT")
    }
}