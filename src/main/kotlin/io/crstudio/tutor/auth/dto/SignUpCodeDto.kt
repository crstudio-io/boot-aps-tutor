package io.crstudio.tutor.auth.dto

data class SignUpCodeDto(
    val email: String,
    val code: String,
    val request: String?,
)