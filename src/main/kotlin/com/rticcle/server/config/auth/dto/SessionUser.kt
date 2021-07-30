package com.rticcle.server.config.auth.dto

import com.rticcle.server.domain.user.User
import java.io.Serializable

class SessionUser (
    var name: String,
    var email: String,
    var picture: String ): Serializable {

    constructor(user: User): this(user.name, user.email, user.picture)
}