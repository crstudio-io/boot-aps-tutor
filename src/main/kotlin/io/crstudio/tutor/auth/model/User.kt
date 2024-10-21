package io.crstudio.tutor.auth.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "USER_TABLE")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(unique = true)
    val email: String?,
    val active: Boolean = false,
    @Column(columnDefinition = "TEXT")
    val request: String?,
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "USER_ROLES",
        joinColumns = [JoinColumn(name = "USER_ID", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "ROLE_ID", referencedColumnName = "id")]
    )
    val roles: MutableSet<Role> = mutableSetOf(),
)

@Entity
@Table(name = "signup_request")
class SignUpRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(unique = true)
    var email: String,
    var verified: Boolean = false,
    val request: String? = null,
    @OneToOne(fetch = FetchType.LAZY)
    var user: User? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    val code: SignUpCode? = null,
)

data class UserDto(
    var email: String?,
) {
    companion object {
        fun fromEntity(entity: User): UserDto = UserDto(
            email = entity.email,
        )
    }
}

@Entity
@Table(name = "signup_code")
class SignUpCode(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    val name: String,
    val code: String,
    val validUntil: LocalDateTime,
)

@Entity
@Table(name = "ROLE_TABLE")
class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    val name: String,
) {
    override fun toString(): String {
        return "ROLE_$name"
    }
}
