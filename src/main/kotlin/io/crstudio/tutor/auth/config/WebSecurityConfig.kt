package io.crstudio.tutor.auth.config

import io.crstudio.tutor.auth.jwt.JwtFilter
import io.crstudio.tutor.auth.jwt.JwtUtils
import io.crstudio.tutor.auth.repo.UserRepo
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.intercept.AuthorizationFilter

@Configuration
class WebSecurityConfig(
    val jwtUtils: JwtUtils,
    val userRepo: UserRepo,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { csrf ->
                csrf.disable()
            }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/auth/signin").permitAll()
                auth.requestMatchers("/test/auth/authenticated").authenticated()
                auth.requestMatchers("/problems/**").authenticated()
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(
                JwtFilter(userRepo, jwtUtils),
                AuthorizationFilter::class.java
            )

        return http.build()
    }

    @Bean
    fun signInSessionTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<String, Long> {
        val template = RedisTemplate<String, Long>()
        template.connectionFactory = connectionFactory
        template.keySerializer = RedisSerializer.string()
        template.valueSerializer = RedisSerializer.java()
        return template
    }
}