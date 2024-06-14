package io.crstudio.tutor.auth.model

import jakarta.persistence.*

@Entity
@Table(name = "USER_TABLE")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    val email: String?
)