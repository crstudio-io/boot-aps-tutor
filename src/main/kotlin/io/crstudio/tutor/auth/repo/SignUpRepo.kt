package io.crstudio.tutor.auth.repo

import io.crstudio.tutor.auth.model.SignupRequest
import org.springframework.data.jpa.repository.JpaRepository

interface SignUpRepo : JpaRepository<SignupRequest, Long> {
    fun findByEmail(email: String): SignupRequest?
}