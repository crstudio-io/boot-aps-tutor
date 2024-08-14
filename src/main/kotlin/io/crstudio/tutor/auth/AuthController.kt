package io.crstudio.tutor.auth

import io.crstudio.tutor.auth.dto.JwtRequestDto
import io.crstudio.tutor.auth.dto.SignUpRequestDto
import io.crstudio.tutor.auth.model.UserDto
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("auth")
class AuthController(
    val authService: AuthService,
    val authFacade: AuthFacade,
) {
    @PostMapping(
        "signin",
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun requestSignIn(@RequestBody jwtRequestDto: JwtRequestDto) =
            authService.requestSignIn(jwtRequestDto)

    @GetMapping(
        "signin"
    )
    fun issueJwt(@RequestParam("token") token: String) =
            authService.finalizeSignIn(token)

    @PostMapping(
        "signup/request",
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun requestSignUp(@RequestBody signUpRequestDto: SignUpRequestDto) =
            authService.requestSignUp(signUpRequestDto)

    @PostMapping(
        "signup/verify",
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun verifySignUp(@RequestParam("token") token: String) =
        authService.finalizeSignUp(token)


    @GetMapping(
        "user-info"
    )
    fun getUserInfo() = UserDto.fromEntity(authFacade.getUser())
}
