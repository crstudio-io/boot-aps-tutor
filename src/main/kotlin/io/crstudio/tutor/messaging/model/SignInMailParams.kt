package io.crstudio.tutor.messaging.model

data class SignInMailParams(
    val email: String,
    val host: String,
    val link: String,
)
