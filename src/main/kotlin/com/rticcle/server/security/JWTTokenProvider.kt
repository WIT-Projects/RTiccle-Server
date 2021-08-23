package com.rticcle.server.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class JWTTokenProvider {
    // TODO: make private
    private val testSecretKey: String =
        "testsecret=sfdkjsdfasdfasdgfgasdfsdfgdgsdfgdfgsdfggdfgsdheertqweijhxcviojmwioerujcvksdfdfjdkfsdf"

    private val tokenPeriod: Long = 1000L * 60L * 10L // 10 minute
    private val refreshTokenPeriod: Long = 1000L * 60L * 60L * 24L * 30L * 3L // 3 weeks

    fun generateToken(userPK: String, role: String): Token {
        val claims: Claims = Jwts.claims().setSubject(userPK)
        claims.put("role", role)

        val date: Date = Date()

        return Token(
            token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(Date(date.time + tokenPeriod))
                .signWith(SignatureAlgorithm.HS256, testSecretKey)
                .compact(),
            refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(Date(date.time + refreshTokenPeriod))
                .signWith(SignatureAlgorithm.HS256, testSecretKey)
                .compact()
        )
    }

    fun verifyToken(jwtToken: String): Boolean{
        return runCatching {
            val tmpClaims: Jws<Claims> = Jwts.parser()
                .setSigningKey(testSecretKey)
                .parseClaimsJws(jwtToken)
            !tmpClaims.body.expiration.before(Date())
        }.getOrDefault(false)
    }

    fun getUserPK(jwtToken: String): String {
        return Jwts.parser().setSigningKey(testSecretKey).parseClaimsJws(jwtToken).body.subject
    }
}