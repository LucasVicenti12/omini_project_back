package com.example.myChat.core.config.service

import com.example.myChat.core.config.entities.SystemUser
import com.example.myChat.core.domain.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AuthorizationService(
    val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails? {
        return userRepository.findByLogin(username)?.run {
            SystemUser(this, mutableListOf(SimpleGrantedAuthority("USER_ADMIN")))
        }
    }
}