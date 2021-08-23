package com.rticcle.server.controller

import com.rticcle.server.security.JWTTokenProvider
import com.rticcle.server.security.Token
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

    @GetMapping("/token/refresh")
    fun refreshAuthorization(@RequestHeader httpHeaders: HttpHeaders): ResponseEntity<Token> {
        val refreshToken: String? = httpHeaders["RefreshToken"]?.get(0)
        return ResponseEntity.ok(
            jwtTokenProvider.refreshToken(refreshToken)
        )
    }
}