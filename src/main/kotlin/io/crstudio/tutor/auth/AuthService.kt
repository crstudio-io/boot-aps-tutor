package io.crstudio.tutor.auth

import io.crstudio.tutor.auth.dto.JwtRequestDto
import io.crstudio.tutor.auth.jwt.JwtUtils
import io.crstudio.tutor.auth.repo.UserRepo
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class AuthService(
    val userRepo: UserRepo,
    val jwtUtils: JwtUtils,
    signInTemplate: RedisTemplate<String, Long>,
) {
    private final val signInOps: ValueOperations<String, Long>
    init {
        signInOps = signInTemplate.opsForValue()
    }
    private final val logger = LoggerFactory.getLogger(this.javaClass)

    fun requestSignIn(jwtRequestDto: JwtRequestDto) {
        val user = userRepo.findByEmail(jwtRequestDto.email)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val token = UUID.randomUUID().toString()
        signInOps[token] = user.id!!
        logger.debug("issuing session for ${user.id} - $token")
        // TODO send email for jwt issue link
    }

    fun finalizeSignIn(sessionToken: String): String {
        val userId = signInOps[sessionToken]
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        if(!userRepo.existsById(userId))
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)

        val jwt = jwtUtils.generateToken(userId)
        logger.debug("issue jwt for: $userId - $jwt")
        return jwt
    }

    // TODO this is a test method for issuing JWT.
    fun testIssueJwt(jwtRequestDto: JwtRequestDto): String {
        val user = userRepo.findByEmail(jwtRequestDto.email)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val jwt = jwtUtils.generateToken(user.id!!)
        logger.debug("issue jwt for: ${user.id} - $jwt")
        return jwt
    }
}