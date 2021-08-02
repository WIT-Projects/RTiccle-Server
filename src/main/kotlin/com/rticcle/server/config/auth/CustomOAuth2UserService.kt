package com.rticcle.server.config.auth

import com.rticcle.server.config.auth.dto.SessionUser
import com.rticcle.server.domain.user.User
import com.rticcle.server.domain.user.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import java.util.*
import javax.servlet.http.HttpSession


@RequiredArgsConstructor
@Service
class CustomOAuth2UserService: OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var httpSession: HttpSession

    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
        val delegate: OAuth2UserService<OAuth2UserRequest, OAuth2User> = DefaultOAuth2UserService()
        val oAuth2User = delegate.loadUser(userRequest)

        // 현재 로그인 진행 중인 서비스를 구분하는 코드
        val registrationId = userRequest
            ?.clientRegistration!!
            .registrationId

        // oauth2 로그인 진행 시 키가 되는 필드값
        val userNameAttributeName = userRequest!!.clientRegistration
            .providerDetails
            .userInfoEndpoint
            .userNameAttributeName

        var attributes: OAuthAttributes = OAuthAttributes(registrationId, userNameAttributeName, oAuth2User.attributes)
        // OAuthAttributes: attribute를 담을 클래스 (개발자가 생성)
        if(registrationId == "kakao"){
            attributes = attributes.ofkakao(userNameAttributeName, oAuth2User.attributes)
        }else if(registrationId == "google"){
            attributes = attributes.ofGoogle(userNameAttributeName, oAuth2User.attributes)
        }else{
            attributes = attributes.ofNaver(userNameAttributeName, oAuth2User.attributes)
        }
        val user: User? = saveOrUpdate(attributes)

        // SessioUser: 세션에 사용자 정보를 저장하기 위한 DTO 클래스 (개발자가 생성)
        httpSession.setAttribute("user", SessionUser(user!!.name, user!!.email, user!!.picture))
        return DefaultOAuth2User(
            Collections.singleton(SimpleGrantedAuthority(user?.role.toString())),
            attributes.attributes,
            attributes.nameAttributeKey
        )
    }

    private fun saveOrUpdate(attributes: OAuthAttributes): User? {
        var user: User? = userRepository.findByEmail(attributes.email)
        if(user == null) {
            user = attributes.toEntity()
        } else {
            user.update(attributes.name, attributes.picture)
        }
        return userRepository.save(user)
    }

}