package io.crstudio.tutor.auth

import io.crstudio.tutor.auth.dto.JwtRequestDto
import io.crstudio.tutor.auth.jwt.JwtUtils
import io.crstudio.tutor.auth.repo.UserRepo
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class AuthService(
    val userRepo: UserRepo,
    val jwtUtils: JwtUtils,
) {
    private final val logger = LoggerFactory.getLogger(this.javaClass)

    // TODO this is a test method for issuing JWT.
    fun testIssueJwt(jwtRequestDto: JwtRequestDto): String {
        val user = userRepo.findByEmail(jwtRequestDto.email)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val jwt = jwtUtils.generateToken(user.id!!)
        logger.debug("issue jwt for: ${user.id} - $jwt")
        return jwt
    }
}