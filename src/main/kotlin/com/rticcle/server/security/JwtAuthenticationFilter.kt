package com.rticcle.server.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

class JwtAuthenticationFilter(private val jwtTokenProvider: JWTTokenProvider) : GenericFilterBean() {

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        // Get Token from Header
        val token: String? = getAuthTokenFromHeader(request as HttpServletRequest)

        // Check token is valid or not
        if (token != null && jwtTokenProvider.verifyToken(token)) {
            // Get User Information
            // TODO Get user info
            val userEmail: String = jwtTokenProvider.getUserPK(token)
            val authentication: Authentication = getAuthentication(userEmail)

            // Save new authentication to security context
            SecurityContextHolder.getContext().authentication = authentication
        }
        chain?.doFilter(request, response)
    }

    private fun getAuthTokenFromHeader(request: HttpServletRequest): String? {
        val auth: String = request.getHeader("Authorization")

        // Split Authorization Since the value is in "Authorization":"Bearer JWT_TOKEN" format
        // TODO Check "Bearer"
        return auth.split(" ")[1]
    }

    private fun getAuthentication(email: String): Authentication {
        return UsernamePasswordAuthenticationToken(
            email, "", listOf(SimpleGrantedAuthority("USER"))
        )
    }
}