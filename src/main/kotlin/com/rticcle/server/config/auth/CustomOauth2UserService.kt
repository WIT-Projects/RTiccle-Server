package com.rticcle.server.config.auth
import com.rticcle.server.config.auth.dto.OAuthAttributes
import com.rticcle.server.domain.user.User

import com.rticcle.server.domain.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomOauth2UserService: OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Autowired
    private lateinit var userRepository: UserRepository

    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
        val delegate: OAuth2UserService<OAuth2UserRequest, OAuth2User> = DefaultOAuth2UserService()
        val oAuth2User: OAuth2User = delegate.loadUser(userRequest)

        val registrationId: String = userRequest
            ?.clientRegistration!!
            .registrationId
        val userNameAttributeName: String = userRequest
            .clientRegistration!!
            .providerDetails
            .userInfoEndpoint
            .userNameAttributeName

        val attributes: OAuthAttributes = OAuthAttributes(registrationId, userNameAttributeName, oAuth2User.attributes)
        val user: User = saveOrUpdate(attributes)

        return DefaultOAuth2User(
            Collections.singleton(SimpleGrantedAuthority("USER")),
            attributes.attributes,
            attributes.nameAttributeKey
        )
    }

    private fun saveOrUpdate(attributes: OAuthAttributes): User {
        var user: User? = userRepository.findByEmail(attributes.email)
        if(user == null) {
            user = attributes.toEntity()
        } else {
            user.update(attributes.name, attributes.picture)
        }

        return userRepository.save(user)
    }
}