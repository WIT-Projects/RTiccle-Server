package com.rticcle.server.security

import com.rticcle.server.domain.user.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val userRepository: UserRepository): UserDetailsService {
    override fun loadUserByUsername(userEmail: String): UserDetails {
        return userRepository.findByEmail(userEmail) ?: throw UsernameNotFoundException("Cannot Find User")
    }
}