package com.rticcle.server.domain.user
import lombok.Getter
import lombok.NoArgsConstructor
import javax.persistence.*

@Getter
@NoArgsConstructor
@Entity
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = Long.MAX_VALUE,

    @Column(nullable = false)
    var name: String = "",

    @Column(nullable = false)
    var email: String = "",

    @Column(nullable = false)
    var picture: String ="",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role:Role
) {
    fun update(name:String, picture: String):User{
        this.name = name
        this.picture = picture

        return this
    }
}

enum class Role {
    GUEST, USER
}