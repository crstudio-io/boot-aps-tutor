package io.crstudio.tutor.auth.repo

import io.crstudio.tutor.auth.model.SignUpRequest
import org.springframework.data.jpa.repository.JpaRepository

interface SignUpRepo : JpaRepository<SignUpRequest, Long> {
    fun findByEmail(email: String): SignUpRequest?
}