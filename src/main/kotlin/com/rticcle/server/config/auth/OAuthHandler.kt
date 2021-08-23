package com.rticcle.server.config.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.rticcle.server.domain.user.Role
import com.rticcle.server.security.JWTTokenProvider
import com.rticcle.server.security.dto.JWTToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OAuthSuccessHandler: AuthenticationSuccessHandler {

    @Autowired
    private lateinit var jwtTokenProvider: JWTTokenProvider

    @Autowired
    private lateinit var objectMapper: ObjectMapper;

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        val oAuth2User: OAuth2User = authentication!!.principal as OAuth2User
        val userPK: String = oAuth2User.attributes["email"] as String

        // TODO: Register User here
        //val user: User = saveOrUpdate(oAuth2User)

        // Generate Token and Write
        val jwtToken: JWTToken = jwtTokenProvider.generateToken(userPK, Role.USER.toString())
        writeTokenResponse(response, jwtToken)
    }

    private fun writeTokenResponse(response: HttpServletResponse?, jwtToken: JWTToken) {
        response?.apply {
            addHeader("Auth", jwtToken.token);
            addHeader("Refresh", jwtToken.refreshToken);
            contentType = "application/json;charset=UTF-8";

            var writer = writer;
            writer.println(objectMapper.writeValueAsString(jwtToken));
            writer.flush();
        }
    }

}
