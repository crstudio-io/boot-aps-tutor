package io.crstudio.tutor.messaging.model

data class SignUpMailParams(
    val email: String,
    val host: String,
    val link: String,
)