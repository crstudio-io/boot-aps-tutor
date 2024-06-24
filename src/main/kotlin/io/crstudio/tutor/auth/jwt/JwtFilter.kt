package io.crstudio.tutor.auth.jwt

import io.crstudio.tutor.auth.config.IdBasedProxyAuthentication
import io.crstudio.tutor.auth.repo.UserRepo
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter


class JwtFilter(
    private val userRepo: UserRepo,
    private val jwtUtils: JwtUtils,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
            ?: run {
                filterChain.doFilter(request, response)
                return
            }
        if (!authHeader.startsWith("Bearer ") || authHeader.length < 8) {
            filterChain.doFilter(request, response)
            return
        }
        val jwt = authHeader.split(" ")[1]
        if (!jwtUtils.validate(jwt)) {
            filterChain.doFilter(request, response)
            return
        }

        val context = SecurityContextHolder.createEmptyContext()
        val claims = jwtUtils.parseClaims(jwt)

        val userId = runCatching {
            claims.subject.toLong()
        }.getOrElse {
            logger.warn("subject cannot be cast to Long: ${claims.subject}")
            filterChain.doFilter(request, response)
            return
        }
        val authClaims = claims["aut"]
        val authorities = if (authClaims is MutableCollection<*>) {
            authClaims.map { GrantedAuthority { it.toString() } }
        } else mutableListOf()

        context.authentication = IdBasedProxyAuthentication(
            authorities,
            userId,
            userRepo
        )
        SecurityContextHolder.setContext(context)

        filterChain.doFilter(request, response)
    }
}