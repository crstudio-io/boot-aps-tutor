package io.crstudio.tutor.auth.repo

import io.crstudio.tutor.auth.model.SignUpCode
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface SignUpCodeRepo : JpaRepository<SignUpCode, Long> {
    fun findByCodeAndValidUntilAfter(code: String, now: LocalDateTime): SignUpCode?
}