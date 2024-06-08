package io.crstudio.tutor.auth.config

import io.crstudio.tutor.auth.jwt.JwtFilter
import io.crstudio.tutor.auth.jwt.JwtUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.intercept.AuthorizationFilter

@Configuration
class WebSecurityConfig(
    val jwtUtils: JwtUtils
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { csrf ->
                csrf.disable()
            }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/auth/signin").permitAll()
                auth.requestMatchers("/auth/authenticated").authenticated()
                auth.requestMatchers("/problem/**").permitAll()
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(
                JwtFilter(jwtUtils),
                AuthorizationFilter::class.java
            )

        return http.build()
    }
}