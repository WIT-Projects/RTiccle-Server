package com.rticcle.server.config.auth.dto

import com.rticcle.server.domain.user.Role
import com.rticcle.server.domain.user.User


class OAuthAttributes (
    var attributes: Map<String, Any>,
    var nameAttributeKey: String,
    var name: String,
    var email: String,
    var picture: String ) {

    companion object {
        fun of(registrationId: String,
               userNameAttributeName: String,
               attributes: Map<String, Any>): OAuthAttributes {
            if("naver" == registrationId) {
                // Naver
                return ofNaver("id", attributes)
            } else if("kakao" == registrationId) {
                // Kakao
                return ofKakao("id", attributes)
            } else {
                // Google
                return ofGoogle(userNameAttributeName, attributes)
            }
        }

        private fun ofGoogle(userNameAttributeName: String,
                             attributes: Map<String, Any>): OAuthAttributes {
            return OAuthAttributes(
                attributes = attributes,
                nameAttributeKey = userNameAttributeName,
                name = attributes["name"].toString(),
                email = attributes["email"].toString(),
                picture = attributes["picture"].toString(),
            )
        }

        private fun ofNaver(userNameAttributeName: String,
                            attributes: Map<String, Any>): OAuthAttributes {
            val naverAttributes: Map<String, Any> = attributes["response"] as Map<String, Any>

            return OAuthAttributes(
                attributes = naverAttributes,
                nameAttributeKey = userNameAttributeName,
                name = naverAttributes["name"].toString(),
                email = naverAttributes["email"].toString(),
                picture = naverAttributes["profile_image"].toString(),
            )
        }

        private fun ofKakao(userNameAttributeName: String,
                            attributes: Map<String, Any>): OAuthAttributes {
            val kakaoAccount:Map<String, Any> = attributes["kakao_account"] as Map<String, Any>
            val kakaoProfile:Map<String, Any> = kakaoAccount["profile"] as Map<String, Any>

            // Add user email to attributes since JWT Token Provider will use "email" property
            val email: String = kakaoAccount["email"].toString()
            val returnAttributes: Map<String, Any> = attributes.plus(Pair("email", email))

            return OAuthAttributes(
                attributes = returnAttributes,
                nameAttributeKey = userNameAttributeName,
                name = kakaoProfile["nickname"].toString(),
                email = email,
                picture = kakaoProfile["profile_image_url"].toString(),
            )
        }
    }

    fun toEntity(): User {
        return User(
            name = name,
            email = email,
            picture = picture,
            role = Role.USER
        )
    }
}