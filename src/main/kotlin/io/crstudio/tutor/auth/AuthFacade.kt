package io.crstudio.tutor.auth

import io.crstudio.tutor.auth.model.User
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

@Component
class AuthFacade {
    fun getUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication
        if (!authentication.isAuthenticated)
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        return authentication.principal as User
    }
}