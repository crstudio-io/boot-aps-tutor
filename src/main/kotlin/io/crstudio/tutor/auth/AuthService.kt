package io.crstudio.tutor.auth

import io.crstudio.tutor.auth.config.SignInSession
import io.crstudio.tutor.auth.dto.JwtRequestDto
import io.crstudio.tutor.auth.jwt.JwtUtils
import io.crstudio.tutor.auth.repo.UserRepo
import io.crstudio.tutor.messaging.EmailProducer
import io.crstudio.tutor.messaging.model.SignInMailParams
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class AuthService(
    val userRepo: UserRepo,
    val jwtUtils: JwtUtils,
    val emailProducer: EmailProducer,
    @Value("\${service.token-front}")
    val tokenPath: String,
    signInHashTemplate: RedisTemplate<String, SignInSession>,
) {
    private final val signInHashOps: ValueOperations<String, SignInSession>

    init {
        signInHashOps = signInHashTemplate.opsForValue()
    }

    private final val logger = LoggerFactory.getLogger(this.javaClass)

    fun requestSignIn(jwtRequestDto: JwtRequestDto) {
        val user = userRepo.findByEmail(jwtRequestDto.email)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val token = UUID.randomUUID().toString()
            .replace("-", "")
        signInHashOps.set("tutor-signin-$token", SignInSession(user.id!!), 10, TimeUnit.MINUTES)
        logger.debug("issuing session for ${user.id} - $token")
        emailProducer.signInEmail(
            SignInMailParams(
                email = user.email!!,
                link = "$tokenPath?token=$token",
            )
        )
    }

    @Transactional
    fun finalizeSignIn(token: String): String {
        val signInSession = signInHashOps.get("tutor-signin-$token")
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val userId = signInSession.userId
        if (signInSession.getIssued()) {
            logger.debug("use pre-issued jwt")
            return signInSession.getToken()
                ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "null token")
        }
        val jwt = jwtUtils.generateToken(userId)
        logger.debug("issue jwt for: $userId - $jwt")
        signInSession.issueToken(jwt)
        signInHashOps.setIfPresent("tutor-signin-$token", signInSession, 1, TimeUnit.MINUTES)
        return jwt
    }
}