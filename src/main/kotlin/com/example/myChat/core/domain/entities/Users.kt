package com.example.myChat.core.domain.entities

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.Base64
import java.util.UUID

data class Users(
        val uuid: UUID?,
        val login: String? = "",
        val passwordUser: String? = null,
        val userType: UserTypes? = UserTypes.USER,
        val email: String? = "",
        val avatar: String? = null,
        val altAvatar: String? = "",
        val userStatus: Int? = 0,
        val name: String? = null,
        val surname: String? = null
) : UserDetails {

    constructor() : this(uuid = null)

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = if (this.userType === UserTypes.ADMIN) {
        mutableListOf(
                SimpleGrantedAuthority("ROLE_ADMIN"),
                SimpleGrantedAuthority("ROLE_USER")
        )
    } else {
        mutableListOf(SimpleGrantedAuthority("ROLE_USER"))
    }

    override fun getPassword(): String = this.passwordUser.toString()

    override fun getUsername(): String = this.login.toString()

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}