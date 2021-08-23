package com.rticcle.server.controller

import com.rticcle.server.domain.user.UserRepository
import com.rticcle.server.security.JWTTokenProvider
import com.rticcle.server.security.dto.JWTToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class TokenController {
    @Autowired
    private lateinit var jwtTokenProvider: JWTTokenProvider

    @Autowired
    private lateinit var userRepository: UserRepository

    @GetMapping("/token/refresh")
    fun refreshAuthorization(@RequestHeader httpHeaders: HttpHeaders): ResponseEntity<JWTToken> {
        val refreshToken: String? = httpHeaders["RefreshToken"]?.get(0)
        return ResponseEntity.ok(
            jwtTokenProvider.refreshToken(refreshToken)
        )
    }

    // Test
    @GetMapping("/token/picture")
    fun getUserPicture(@RequestHeader httpHeaders: HttpHeaders): ResponseEntity<String> {
        val jwtToken = jwtTokenProvider.getJwtTokenFromHeader(httpHeaders)
        val email = jwtTokenProvider.getUserPK(jwtToken)
        // Temp
        return ResponseEntity.ok(
            userRepository.findByEmail(email)?.picture ?: "NOT FOUND"
        )
    }
}