package io.crstudio.tutor.auth

import io.crstudio.tutor.auth.dto.JwtRequestDto
import io.crstudio.tutor.auth.jwt.JwtUtils
import io.crstudio.tutor.auth.repo.UserRepo
import io.crstudio.tutor.messaging.EmailProducer
import io.crstudio.tutor.messaging.model.SignInMailParams
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class AuthService(
    val userRepo: UserRepo,
    val jwtUtils: JwtUtils,
    val emailProducer: EmailProducer,
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
            .replace("-", "")
        signInOps.set("tutor-signin-$token", user.id!!, 10, TimeUnit.MINUTES)
        logger.debug("issuing session for ${user.id} - $token")
        emailProducer.signInEmail(SignInMailParams(
            email = user.email!!,
            link = "/auth/signin?token=$token",
        ))
    }

    fun finalizeSignIn(token: String): String {
        val userId = signInOps.getAndDelete("tutor-signin-$token")
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        if(!userRepo.existsById(userId))
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
        val jwt = jwtUtils.generateToken(userId)
        logger.debug("issue jwt for: $userId - $jwt")
        return jwt
    }
}