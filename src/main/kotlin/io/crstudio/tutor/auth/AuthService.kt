package io.crstudio.tutor.auth

import io.crstudio.tutor.auth.config.SignInSession
import io.crstudio.tutor.auth.config.SignUpSession
import io.crstudio.tutor.auth.dto.JwtRequestDto
import io.crstudio.tutor.auth.dto.SignUpCodeDto
import io.crstudio.tutor.auth.dto.SignUpRequestDto
import io.crstudio.tutor.auth.jwt.JwtUtils
import io.crstudio.tutor.auth.model.SignUpRequest
import io.crstudio.tutor.auth.model.User
import io.crstudio.tutor.auth.repo.SignUpCodeRepo
import io.crstudio.tutor.auth.repo.SignUpRepo
import io.crstudio.tutor.auth.repo.UserRepo
import io.crstudio.tutor.messaging.EmailProducer
import io.crstudio.tutor.messaging.model.SignInMailParams
import io.crstudio.tutor.messaging.model.SignUpMailParams
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.util.UriComponentsBuilder
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class AuthService (
    val userRepo: UserRepo,
    val jwtUtils: JwtUtils,
    val emailProducer: EmailProducer,
    val signUpRepo: SignUpRepo,
    val signUpCodeRepo: SignUpCodeRepo,
    @Value("\${service.front-host}")
    val frontHost: String,
    @Value("\${service.token-front}")
    val tokenPath: String,
    @Value("\${service.signup-front}")
    val signUpPath: String,
    signInHashTemplate: RedisTemplate<String, SignInSession>,
    signUpHashTemplate: RedisTemplate<String, SignUpSession>,
) : UserDetailsService{
    private final val signInOps: ValueOperations<String, SignInSession> = signInHashTemplate.opsForValue()
    private final val signUpOps: ValueOperations<String, SignUpSession> = signUpHashTemplate.opsForValue()

    private final val logger = LoggerFactory.getLogger(this.javaClass)

    @Override
    override fun loadUserByUsername(username: String): UserDetails
            = throw ResponseStatusException(HttpStatus.NOT_IMPLEMENTED)

    @Transactional
    fun requestSignIn(jwtRequestDto: JwtRequestDto) {
        val user = userRepo.findByEmail(jwtRequestDto.email)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        if (!user.active)
            throw ResponseStatusException(HttpStatus.FORBIDDEN)
        val token = UUID.randomUUID().toString()
            .replace("-", "")
        signInOps.set("tutor-signin-$token", SignInSession(user.id!!), 10, TimeUnit.MINUTES)
        logger.debug("issuing session for ${user.id} - $token")
        emailProducer.signInEmail(
            SignInMailParams(
                email = user.email!!,
                host = frontHost,
                link = "$frontHost$tokenPath?token=$token",
            )
        )
        logger.debug("signin link: $frontHost$tokenPath?token=$token")
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

    @Transactional
    fun signUpRequest(dto: SignUpRequestDto) {
        if (userRepo.existsByEmail(dto.email))
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Already signed up")

        signUpRepo.save(SignUpRequest(
            email = dto.email,
            request = dto.request,
        ))

        sendSignUpEmail(dto.email)
    }

    @Transactional
    fun signUpCode(dto: SignUpCodeDto) {
        if (userRepo.existsByEmail(dto.email))
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Already signed up")
        signUpRepo.save(SignUpRequest(
            email = dto.email,
            code = signUpCodeRepo.findByCodeAndValidUntilAfter(dto.code, LocalDateTime.now())
                ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "invalid code"),
            request = dto.request
        ))

        sendSignUpEmail(dto.email, true)
    }

    private fun sendSignUpEmail(email: String, withCode: Boolean = false) {
        val token = UUID.randomUUID().toString()
            .replace("-", "")
        signUpOps.set(
            "tutor-signup-$token", SignUpSession(
                email = email
            ), 10, TimeUnit.MINUTES
        )
        val linkBuilder =  UriComponentsBuilder.fromHttpUrl("$frontHost$signUpPath")
            .queryParam("token", token)
        if (withCode) linkBuilder.queryParam("code")

        logger.debug("signup session for ${email}")
        emailProducer.signUpEmail(
            SignUpMailParams(
                email = email,
                host = frontHost,
                link = linkBuilder.build().toUriString(),
            )
        )
        logger.debug("signup link: ${linkBuilder.build().toUriString()}")
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

        val signUpRequest = signUpRepo.findByEmail(signUpSession.email)
            ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
        signUpRequest.verified = true
        if (userRepo.existsByEmail(signUpRequest.email)) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Already signed up")
        }
        val active = signUpRequest.code?.validUntil?.isAfter(LocalDateTime.now())
            ?: false
        val user = userRepo.save(User(
            email = signUpSession.email,
            active = active,
            request = signUpRequest.request
        ))
        signUpRequest.user = userRepo.save(user)
        signUpRepo.save(signUpRequest)
        signUpSession.accepted = true
        logger.debug("signup request saved for user: ${signUpSession.email}")
        signUpOps.setIfPresent("tutor-signup-$token", signUpSession, 1, TimeUnit.MINUTES)
    }
}
