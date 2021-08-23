package com.rticcle.server.security.dto

data class JWTToken (
    val token: String,
    val refreshToken: String
)