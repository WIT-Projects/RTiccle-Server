package com.rticcle.server.security

data class Token (
    val token: String,
    val refreshToken: String
)