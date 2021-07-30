package com.rticcle.server.config.auth.dto

import com.rticcle.server.domain.user.Role
import com.rticcle.server.domain.user.User


class OAuthAttributes (
    var attributes: Map<String, Any>,
    var nameAttributeKey: String,
    var name: String,
    var email: String,
    var picture: String ) {

    constructor(registrationId: String, userNameAttributeName: String, attributes: Map<String, Any>): this(
        // google
        name = attributes["name"] as String,
        email = attributes["email"] as String,
        picture = attributes["picture"] as String,
        attributes = attributes,
        nameAttributeKey = userNameAttributeName
    )

    fun toEntity(): User {
        return User(
            name = name,
            email = email,
            picture = picture,
            role = Role.ROLE_GUEST
        )
    }
}