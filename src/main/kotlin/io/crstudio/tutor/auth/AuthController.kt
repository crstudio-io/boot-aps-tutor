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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun requestSignIn(@RequestBody jwtRequestDto: JwtRequestDto) {
        authService.requestSignIn(jwtRequestDto)
    }

    @GetMapping(
        "signin"
    )
    fun issueJwt(@RequestParam("token") token: String): String {
        return authService.finalizeSignIn(token)
    }
}
