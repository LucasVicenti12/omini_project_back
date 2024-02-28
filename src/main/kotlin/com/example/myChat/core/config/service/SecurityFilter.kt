package com.example.myChat.core.config.service

import com.example.myChat.core.domain.repository.UserRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class SecurityFilter : OncePerRequestFilter() {

    @Autowired
    lateinit var tokenService: TokenService

    @Autowired
    lateinit var userRepository: UserRepository

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = recoverToken(request)

        if (token != "") {
            val login = tokenService.validateToken(token)
            val user = userRepository.findByLogin(login)

            val authentication = UsernamePasswordAuthenticationToken(user, null, user?.authorities)

            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }

    private fun recoverToken(request: HttpServletRequest): String {
        var token = ""
        request.cookies?.forEach {
            if (it.name.equals("USER_SESSION", true)) {
                token = it.value
            }
        }
        return token
    }
}