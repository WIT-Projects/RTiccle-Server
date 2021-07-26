package com.rticcle.server.domain.user
import javax.persistence.Entity

@Entity
class User (
    var userEmail: String = "",
    var userPassword: String = ""
)