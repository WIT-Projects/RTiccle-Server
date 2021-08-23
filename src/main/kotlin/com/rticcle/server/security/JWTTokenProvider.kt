package com.rticcle.server.security

import com.rticcle.server.domain.user.Role
import com.rticcle.server.security.dto.JWTToken
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import java.lang.RuntimeException
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class JWTTokenProvider {
    // TODO: make private
    private val testSecretKey: String =
        "testsecret=sfdkjsdfasdfasdgfgasdfsdfgdgsdfgdfgsdfggdfgsdheertqweijhxcviojmwioerujcvksdfdfjdkfsdf"

    private val tokenPeriod: Long = 1000L * 60L * 10L // 10 minute
    private val refreshTokenPeriod: Long = 1000L * 60L * 60L * 24L * 30L * 3L // 3 weeks

    fun generateToken(userPK: String, role: String): JWTToken {
        val claims: Claims = Jwts.claims().setSubject(userPK)
        claims.put("role", role)

        val date: Date = Date()

        return JWTToken(
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

    fun refreshToken(refreshToken: String?): JWTToken {
        if (refreshToken != null && verifyToken(refreshToken)) {
            val email: String = getUserPK(refreshToken);
            return generateToken(email, Role.USER.toString());
        }
        // TODO Fix Exception
        throw RuntimeException()
    }

    fun getUserPK(jwtToken: String): String {
        return Jwts.parser().setSigningKey(testSecretKey).parseClaimsJws(jwtToken).body.subject
    }

    fun getJwtTokenFromHeader(headers: HttpHeaders): String {
        val jwtToken: String? = headers["Authorization"]?.get(0)

        // Substring Authorization Since the value is in "Authorization":"Bearer JWT_TOKEN" format
        if(jwtToken != null && jwtToken.startsWith("Bearer ")) {
            return jwtToken.substring(7)
        }
        // TODO Fix Exception
        throw RuntimeException()
    }
}