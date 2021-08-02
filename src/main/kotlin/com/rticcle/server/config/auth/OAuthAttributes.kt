package com.rticcle.server.config.auth

import com.rticcle.server.domain.user.Role
import com.rticcle.server.domain.user.User
import lombok.Getter

@Getter
class OAuthAttributes(
    var attributes:Map<String, Any>,
    var nameAttributeKey: String,
    var name: String,
    var email: String,
    var picture: String) {

    constructor(registrationId: String, userNameAttributeName: String, attributes: Map<String, Any>): this(
        // google
        name = attributes["name"] as String,
        email = attributes["email"] as String,
        picture = attributes["picture"] as String,
        attributes = attributes,
        nameAttributeKey = userNameAttributeName
    )

    fun of(registrationId: String, userNameAttributeName: String, attributes: Map<String, Any>): OAuthAttributes {
        if("kakao" == registrationId){
            return ofkakao("id",attributes)
        }
        return ofGoogle(userNameAttributeName, attributes)
    }

    fun ofkakao(userNameAttributeName: String, attributes: Map<String, Any>): OAuthAttributes{
        var response:Map<String, Any> = attributes["kakao_account"] as Map<String, Any>
        var profile:Map<String, Any> = response["profile"] as Map<String, Any>
        return OAuthAttributes(
            name = profile["nickname"].toString(),
            email = response["email"].toString(),
            picture = profile["profile_image_url"].toString(),
            attributes = attributes,
            nameAttributeKey = userNameAttributeName
        )
    }

    fun ofGoogle(userNameAttributeName: String, attributes: Map<String, Any>):OAuthAttributes{
        return OAuthAttributes(
            name = attributes["name"] as String,
            email = attributes["email"] as String,
            picture = attributes["picture"] as String,
            attributes = attributes,
            nameAttributeKey = userNameAttributeName
        )
    }

    fun ofNaver(userNameAttributeName: String, attributes: Map<String, Any>):OAuthAttributes{
        return OAuthAttributes(
            name = attributes["name"] as String,
            email = attributes["email"] as String,
            picture = attributes["profile_image"] as String,
            attributes = attributes,
            nameAttributeKey = userNameAttributeName
        )
    }

    fun toEntity(): User? {
        return User(
            name = name,
            email = email,
            picture = picture,
            role = Role.GUEST
        )
    }
}