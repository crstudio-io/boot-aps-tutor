package io.crstudio.tutor.auth

import io.crstudio.tutor.auth.config.SignInSession
import io.crstudio.tutor.auth.config.SignUpSession
import io.crstudio.tutor.auth.dto.JwtRequestDto
import io.crstudio.tutor.auth.dto.SignUpRequestDto
import io.crstudio.tutor.auth.jwt.JwtUtils
import io.crstudio.tutor.auth.model.User
import io.crstudio.tutor.auth.repo.UserRepo
import io.crstudio.tutor.messaging.EmailProducer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.data.repository.findByIdOrNull
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
    @Value("\${service.front-host}")
    val frontHost: String,
    @Value("\${service.token-front}")
    val tokenPath: String,
    @Value("\${service.signup-front}")
    val signUpPath: String,
    signInHashTemplate: RedisTemplate<String, SignInSession>,
    signUpHashTemplate: RedisTemplate<String, SignUpSession>,
) {
    private final val signInOps: ValueOperations<String, SignInSession> = signInHashTemplate.opsForValue()
    private final val signUpOps: ValueOperations<String, SignUpSession> = signUpHashTemplate.opsForValue()

    private final val logger = LoggerFactory.getLogger(this.javaClass)

    fun requestSignIn(jwtRequestDto: JwtRequestDto) {
        val user = userRepo.findByEmail(jwtRequestDto.email)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        if (!user.active)
            throw ResponseStatusException(HttpStatus.FORBIDDEN)
        val token = UUID.randomUUID().toString()
            .replace("-", "")
        signInOps.set("tutor-signin-$token", SignInSession(user.id!!), 10, TimeUnit.MINUTES)
        logger.debug("issuing session for ${user.id} - $token")
//        emailProducer.signInEmail(
//            SignInMailParams(
//                email = user.email!!,
//                host = frontHost,
//                link = "$frontHost$tokenPath?token=$token",
//            )
//        )
        logger.debug("$frontHost$tokenPath?token=$token")
    }

    @Transactional
    fun finalizeSignIn(token: String): String {
        val signInSession = signInOps.get("tutor-signin-$token")
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
        signInOps.setIfPresent("tutor-signin-$token", signInSession, 1, TimeUnit.MINUTES)
        return jwt
    }

    fun requestSignUp(signUpRequestDto: SignUpRequestDto) {
        if (userRepo.existsByEmail(signUpRequestDto.email))
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Already signed up")
        val user = userRepo.save(User(
            email = signUpRequestDto.email,
            active = false,
            reqAccepted = false,
            request = signUpRequestDto.request,
        ))
        val token = UUID.randomUUID().toString()
            .replace("-", "")
        signUpOps.set("tutor-signup-$token", SignUpSession(
            userId = user.id!!,
            email = signUpRequestDto.email,
            request = signUpRequestDto.request
        ), 10, TimeUnit.MINUTES)
        logger.debug("signup session for ${signUpRequestDto.email}")
//        emailProducer.signUpEmail(
//            SignUpMailParams(
//                email = user.email!!,
//                host = frontHost,
//                link = "$frontHost$tokenPath?token=$token",
//            )
//        )
        logger.debug("$frontHost$signUpPath?token=$token")
    }

    @Transactional
    fun finalizeSignUp(token: String) {
        val signUpSession = signUpOps.get("tutor-signup-$token")
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        logger.debug(signUpSession.toString())
        if (signUpSession.accepted) {
            logger.debug("already accepted")
            return
        }
        val user = userRepo.findByIdOrNull(signUpSession.userId)
            ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
        user.reqAccepted = true
        userRepo.save(user)
        signUpSession.accepted = true
        logger.debug("signup request saved for user: ${signUpSession.email}")
        signUpOps.setIfPresent("tutor-signup-$token", signUpSession, 1, TimeUnit.MINUTES)
    }
}