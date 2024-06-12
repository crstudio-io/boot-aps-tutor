package io.crstudio.tutor.auth.model

import io.crstudio.tutor.auth.repo.UserIdProxy
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.web.server.ResponseStatusException

/**
 * Authentication that stores identity, and use it to retrieve
 * actual information via Proxy
 */
class IdBasedProxyAuthentication<P, ID>(
    authorities: Collection<GrantedAuthority>,
    val userId: ID,
    val userProxy: UserIdProxy<P, ID>,
): AbstractAuthenticationToken(
    authorities
) {
    val user by lazy {
        userProxy.findById(userId).orElseThrow {
            ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    init {
        isAuthenticated = true
    }

    override fun getCredentials(): ID = userId
    override fun getPrincipal(): P = user
}