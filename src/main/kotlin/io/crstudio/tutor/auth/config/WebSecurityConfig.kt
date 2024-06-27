package io.crstudio.tutor.auth.config

import io.crstudio.tutor.auth.jwt.JwtFilter
import io.crstudio.tutor.auth.jwt.JwtUtils
import io.crstudio.tutor.auth.repo.UserRepo
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.intercept.AuthorizationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class WebSecurityConfig(
    val jwtUtils: JwtUtils,
    val userRepo: UserRepo,
    @Value("\${service.front-host}")
    val frontOrigin: String,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            csrf {
                disable()
            }
            cors {
                configurationSource = corsConfigurationSource()
            }
            authorizeHttpRequests {
                authorize("/auth/signin", permitAll)
//                authorize("/test/auth/authenticated", authenticated)
                authorize("/problems/*/solutions/**", authenticated)
                authorize(HttpMethod.POST, "/problems/**", hasAnyRole("ADMIN", "POWER_USER"))
                authorize(HttpMethod.PUT, "/problems/**", hasAnyRole("ADMIN", "POWER_USER"))
                authorize(HttpMethod.DELETE, "/problems/**", hasAnyRole("ADMIN", "POWER_USER"))
                authorize(HttpMethod.GET, "/problems", permitAll)
                authorize(HttpMethod.GET, "/problems/*", permitAll)
                authorize(anyRequest, authenticated)
            }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
//            addFilterBefore(JwtFilter(userRepo, jwtUtils), AuthorizationFilter::class.java)
            addFilterBefore<AuthorizationFilter>(JwtFilter(userRepo, jwtUtils))
        }

        return http.build()
    }

    @Bean
    fun signInHashTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<String, SignInSession> {
        val template = RedisTemplate<String, SignInSession>()
        template.connectionFactory = connectionFactory
        template.keySerializer = RedisSerializer.string()
        template.valueSerializer = RedisSerializer.json()
        template.setEnableTransactionSupport(true)
        return template
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf(frontOrigin)
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
        configuration.allowedHeaders = listOf("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}