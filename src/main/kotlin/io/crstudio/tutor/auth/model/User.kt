package io.crstudio.tutor.auth.model

import jakarta.persistence.*

@Entity
@Table(name = "USER_TABLE")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    val email: String?,
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "USER_ROLES",
        joinColumns = [JoinColumn(name = "USER_ID", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "ROLE_ID", referencedColumnName = "id")]
    )
    val roles: MutableSet<Role> = mutableSetOf(),
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
