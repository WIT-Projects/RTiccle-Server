package com.rticcle.server.domain.user
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = Long.MAX_VALUE,

    @Column(nullable = false)
    var name: String = "",

    @Column(nullable = false)
    var email: String = "",

    var picture: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role
): UserDetails {
    fun update(name: String, picture: String): User {
        this.name = name;
        this.picture = picture

        return this
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf<GrantedAuthority>(
            SimpleGrantedAuthority(role.toString())
        )
    }
    override fun getPassword(): String = "Nothing" // This is an unused property
    override fun getUsername(): String = email
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}

enum class Role {
    GUEST, USER
}