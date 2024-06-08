package io.crstudio.tutor.auth

import io.crstudio.tutor.auth.dto.JwtRequestDto
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("auth")
class AuthController(
    val authService: AuthService
) {
    @PostMapping(
        "signin",
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun signin(@RequestBody jwtRequestDto: JwtRequestDto): String {
        return authService.testIssueJwt(jwtRequestDto)
    }

    // TODO this is a test method to test authenticated requests
    @GetMapping(
        "authenticated"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun testAuthenticated() {}
}