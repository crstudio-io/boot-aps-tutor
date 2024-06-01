package io.crstudio.tutor.auth.entity

import jakarta.persistence.*

@Entity
@Table(name = "USER_TABLE")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    val email: String?
)