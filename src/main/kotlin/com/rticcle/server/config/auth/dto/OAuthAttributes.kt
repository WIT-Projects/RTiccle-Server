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
                println("naver, $registrationId, $userNameAttributeName")
                return ofNaver("id", attributes)
            } else if("Kakao" == registrationId) {
                // Kakao
                println("Kakao, $registrationId, $userNameAttributeName")
                return ofKakao("id", attributes)
            } else {
                // Google
                println("google, $registrationId, $userNameAttributeName")
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
            val response: Map<String, Any> = attributes["response"] as Map<String, Any>

            return OAuthAttributes(
                attributes = response,
                nameAttributeKey = userNameAttributeName,
                name = attributes["name"].toString(),
                email = attributes["email"].toString(),
                picture = attributes["profile_image"].toString(),
            )
        }

        private fun ofKakao(userNameAttributeName: String,
                            attributes: Map<String, Any>): OAuthAttributes {
            val response:Map<String, Any> = attributes["kakao_account"] as Map<String, Any>
            val profile:Map<String, Any> = response["profile"] as Map<String, Any>

            return OAuthAttributes(
                attributes = attributes,
                nameAttributeKey = userNameAttributeName,
                name = profile["nickname"].toString(),
                email = response["email"].toString(),
                picture = profile["profile_image_url"].toString(),
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