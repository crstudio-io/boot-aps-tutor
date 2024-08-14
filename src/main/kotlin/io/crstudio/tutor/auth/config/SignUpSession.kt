package io.crstudio.tutor.auth.config

import com.fasterxml.jackson.annotation.JsonProperty

data class SignUpSession (
    @JsonProperty("userId")
    val userId: Long,
    @JsonProperty("email")
    val email: String,
    @JsonProperty("accepted")
    var accepted: Boolean = false,
    @JsonProperty("request")
    var request: String,
)