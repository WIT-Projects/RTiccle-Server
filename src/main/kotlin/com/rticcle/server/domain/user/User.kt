package com.rticcle.server.domain.user
import javax.persistence.*

@Entity
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = Long.MAX_VALUE,

    @Column(nullable = false)
    var userEmail: String = "",

    @Column(nullable = false)
    var userPassword: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role
) {
}

enum class Role {
    ROLE_GUEST, ROLE_USER
}