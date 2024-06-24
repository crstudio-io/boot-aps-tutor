package io.crstudio.tutor.auth

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Profile("dev")
@RestController
@RequestMapping("test/auth")
class AuthTestController {
    private final val logger = LoggerFactory.getLogger(this.javaClass)

    @GetMapping(
        "authenticated"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun testAuthenticated() {
        logger.debug(SecurityContextHolder.getContext().toString())
    }
}