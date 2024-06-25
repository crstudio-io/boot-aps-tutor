package io.crstudio.tutor.auth.config

class SignInSession(
    val userId: Long,
    private var issued: Boolean = false,
    private var token: String? = null,
) {
    // TODO maybe record some info to prevent remote jwt issue?
    fun issueToken(token: String) {
        this.token = token
        this.issued = true
    }

    fun getIssued() = issued
    fun retrieveToken(): String = token
        ?: throw IllegalStateException("Token is null")
}