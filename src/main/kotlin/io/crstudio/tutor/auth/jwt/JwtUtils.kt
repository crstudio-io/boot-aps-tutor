package io.crstudio.tutor.auth.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtils(
    @Value("\${jwt.secret}")
    jwtSecret: String
) {
    private final val logger = LoggerFactory.getLogger(this.javaClass)
    private final val signingKey: SecretKey
    private final val jwtParser: JwtParser

    init {
        signingKey = Keys.hmacShaKeyFor(jwtSecret.toByteArray())
        jwtParser = Jwts.parserBuilder().setSigningKey(signingKey).build()
    }

    fun generateToken(userId: Long, authorities: MutableCollection<String> = mutableListOf()): String {
        val now = Instant.now()
        val jwtClaims = Jwts.claims()
            .setSubject(userId.toString())
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plusSeconds(60)))

        jwtClaims["aut"] = authorities

        return Jwts.builder()
            .setClaims(jwtClaims)
            .signWith(signingKey)
            .compact()
    }

    fun validate(jwt: String): Boolean = try {
        jwtParser.parseClaimsJws(jwt)
        true
    } catch (e: Exception) {
        logger.warn("invalid JWT: ${e.message}")
        false
    }

    fun parseClaims(jwt: String): Claims = jwtParser
        .parseClaimsJws(jwt)
        .body
}
