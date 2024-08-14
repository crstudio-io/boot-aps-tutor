package io.crstudio.tutor.auth.repo

import io.crstudio.tutor.auth.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepo : JpaRepository<User, Long>, UserIdProxy<User, Long> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
}
