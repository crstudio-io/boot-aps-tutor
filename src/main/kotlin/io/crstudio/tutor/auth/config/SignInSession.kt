package io.crstudio.tutor.auth.config

import com.fasterxml.jackson.annotation.JsonProperty

class SignInSession(
    @JsonProperty("userId")
    val userId: Long,
    @JsonProperty("issued")
    private var issued: Boolean = false,
    @JsonProperty("token")
    private var token: String? = null,
) {
    // TODO maybe record some info to prevent remote jwt issue?
    fun issueToken(token: String) {
        this.token = token
        this.issued = true
    }

    fun getIssued() = issued
    fun getToken(): String? = token
}